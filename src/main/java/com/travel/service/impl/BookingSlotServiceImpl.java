package com.travel.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.travel.dto.BookingSlotCreateDTO;
import com.travel.entity.BookingSlot;
import com.travel.exception.BusinessException;
import com.travel.mapper.BookingSlotMapper;
import com.travel.service.BookingSlotService;
import com.travel.vo.BookingSlotVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingSlotServiceImpl implements BookingSlotService {

  private static final int STATUS_DISABLED = 0;
  private static final int STATUS_ENABLED = 1;
  private static final Set < String > VALID_TARGET_TYPES = Set.of( "SCENIC", "ENTERTAINMENT" );

  private final BookingSlotMapper slotMapper;

  public BookingSlotServiceImpl( BookingSlotMapper slotMapper ) {
    this.slotMapper = slotMapper;
  }

  /**
   * 归一化并校验预约对象类型。
   * targetType 来自请求参数，入库前统一转成大写白名单值，避免出现 scenic/SCENIC 混用。
   */
  private String normalizeTargetType( String targetType ) {
    if ( targetType == null || targetType.isBlank() ) {
      throw new BusinessException( 400, "targetType不能为空" );
    }
    String upper = targetType.trim().toUpperCase();
    if ( !VALID_TARGET_TYPES.contains( upper ) ) {
      throw new BusinessException( 400, "不支持的targetType：" + targetType );
    }
    return upper;
  }

  @Override
  public Long createSlot( BookingSlotCreateDTO dto ) {
    validateCreateDTO( dto );

    String targetType = normalizeTargetType( dto.getTargetType() );
    ensureSlotNotExists( targetType, dto );

    BookingSlot slot = new BookingSlot();
    BeanUtils.copyProperties( dto, slot );
    slot.setTargetType( targetType );
    slot.setBookedCount( 0 );
    slot.setStatus( STATUS_ENABLED );
    slot.setIsDeleted( 0 );
    slotMapper.insert( slot );
    return slot.getId() != null ? slot.getId() : 0L;
  }

  @Override
  public List < BookingSlotVO > listAvailableSlots( String targetType, Long targetId ) {
    String normalized = targetType == null || targetType.isBlank() ? null : normalizeTargetType( targetType );
    if ( targetId != null && targetId <= 0 ) {
      throw new BusinessException( 400, "targetId必须大于0" );
    }
    List < BookingSlot > slots = slotMapper.selectList(
        Wrappers. < BookingSlot > lambdaQuery()
            .eq( normalized != null, BookingSlot :: getTargetType, normalized )
            .eq( targetId != null, BookingSlot :: getTargetId, targetId )
            .eq( BookingSlot :: getStatus, STATUS_ENABLED )
            .ge( BookingSlot :: getSlotDate, LocalDate.now() ) // 先在数据库层过滤过去日期，减少返回数据量。
            .orderByAsc( BookingSlot :: getSlotDate )
            .orderByAsc( BookingSlot :: getStartTime )
    );
    return slots.stream()
        .filter( slot -> !isStartedOrExpired( slot ) )
        .map( this :: toVO )
        .collect( Collectors.toList() );
  }

  @Override
  public void disableSlot( Long slotId ) {
    BookingSlot slot = slotMapper.selectById( slotId );
    if ( slot == null ) {
      throw new BusinessException( 404, "时段不存在" );
    }
    slot.setStatus( STATUS_DISABLED );
    slotMapper.updateById( slot );
  }

  private void validateCreateDTO( BookingSlotCreateDTO dto ) {
    if ( dto.getTargetId() == null || dto.getTargetId() <= 0 ) {
      throw new BusinessException( 400, "targetId必须大于0" );
    }
    if ( dto.getSlotDate() == null || dto.getStartTime() == null || dto.getEndTime() == null ) {
      throw new BusinessException( 400, "预约日期、开始时间和结束时间不能为空" );
    }
    if ( !dto.getEndTime().isAfter( dto.getStartTime() ) ) {
      throw new BusinessException( 400, "结束时间必须晚于开始时间" );
    }
    if ( isStartedOrExpired( dto.getSlotDate(), dto.getStartTime() ) ) {
      throw new BusinessException( 400, "不能创建已经开始或过期的时段" );
    }
  }

  /**
   * 同一个对象、同一天、同一开始时间只允许存在一个时段。
   * 真正防并发重复仍应在数据库加唯一索引，这里先给正常请求提供清晰错误信息。
   */
  private void ensureSlotNotExists( String targetType, BookingSlotCreateDTO dto ) {
    Long count = slotMapper.selectCount(
        Wrappers. < BookingSlot > lambdaQuery()
            .eq( BookingSlot :: getTargetType, targetType )
            .eq( BookingSlot :: getTargetId, dto.getTargetId() )
            .eq( BookingSlot :: getSlotDate, dto.getSlotDate() )
            .eq( BookingSlot :: getStartTime, dto.getStartTime() )
    );
    if ( count > 0 ) {
      throw new BusinessException( 400, "该对象在这个日期和开始时间已经配置过预约时段" );
    }
  }

  private boolean isStartedOrExpired( BookingSlot slot ) {
    return isStartedOrExpired( slot.getSlotDate(), slot.getStartTime() );
  }

  private boolean isStartedOrExpired( LocalDate slotDate, LocalTime startTime ) {
    LocalDate today = LocalDate.now();
    return slotDate.isBefore( today ) || slotDate.isEqual( today ) && !startTime.isAfter( LocalTime.now() );
  }

  private BookingSlotVO toVO( BookingSlot slot ) {
    BookingSlotVO vo = new BookingSlotVO();
    BeanUtils.copyProperties( slot, vo );
    vo.setRemainingCount( slot.getCapacity() - slot.getBookedCount() );
    return vo;
  }
}
