package com.travel.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.entity.BookingOrder;
import com.travel.enums.BookingStatus;
import com.travel.exception.BusinessException;
import com.travel.mapper.BookingOrderMapper;
import com.travel.mapper.BookingSlotMapper;
import com.travel.service.BookingOrderService;
import com.travel.vo.BookingOrderAdminVO;
import com.travel.vo.BookingOrderVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingOrderServiceImpl implements BookingOrderService {

  private static final long MAX_PAGE_SIZE = 100L;

  private final BookingOrderMapper orderMapper;
  private final BookingSlotMapper slotMapper;

  public BookingOrderServiceImpl( BookingOrderMapper orderMapper, BookingSlotMapper slotMapper ) {
    this.orderMapper = orderMapper;
    this.slotMapper = slotMapper;
  }

  @Override
  @Transactional
  public Long createOrder( Long userId, Long slotId ) {
    if ( userId == null || slotId == null ) {
      throw new BusinessException( 400, "用户ID和时段ID不能为空" );
    }

    // 扣名额和插订单必须在同一个事务里：任一步失败，都要让数据库回到下单前的状态。
    int affected = slotMapper.deductCapacity( slotId );
    if ( affected == 0 ) {
      throw new BusinessException( 400, "该时段不存在、已满、已下架或已开始，请选择其他时段" );
    }

    BookingOrder order = new BookingOrder();
    order.setUserId( userId );
    order.setSlotId( slotId );
    order.setStatus( BookingStatus.PENDING.getCode() );
    order.setIsDeleted( 0 );
    try {
      orderMapper.insert( order );
    }
    catch ( org.springframework.dao.DuplicateKeyException e ) {
      // 撞到 user_id + slot_id 唯一索引时抛业务异常；事务会回滚上面的名额扣减。
      throw new BusinessException( 400, "你已经预订过这个时段了" );
    }
    return order.getId();
  }

  @Override
  public void confirmOrder( Long orderId ) {
    BookingOrder order = orderMapper.selectById( orderId );
    if ( order == null ) {
      throw new BusinessException( 404, "订单不存在" );
    }
    BookingStatus current = BookingStatus.fromCode( order.getStatus() );
    if ( !current.canTransitionTo( BookingStatus.CONFIRMED ) ) {
      throw new BusinessException( 400, "当前状态[" + current.getDesc() + "]不能确认" );
    }
    int affected = orderMapper.updateStatusIfCurrent(
        orderId,
        current.getCode(),
        BookingStatus.CONFIRMED.getCode()
    );
    if ( affected == 0 ) {
      throw new BusinessException( 409, "订单状态已变化，请刷新后重试" );
    }
  }

  @Override
  @Transactional
  public void cancelOrder( Long userId, Long orderId, boolean isAdmin ) {
    BookingOrder order = orderMapper.selectById( orderId );
    if ( order == null ) {
      throw new BusinessException( 404, "订单不存在" );
    }

    // 权限判断放在 Service 层，避免未来新增入口时绕过“只能操作自己的订单”这条业务规则。
    if ( !isAdmin && !order.getUserId().equals( userId ) ) {
      throw new BusinessException( 403, "无权操作他人的订单" );
    }
    BookingStatus current = BookingStatus.fromCode( order.getStatus() );
    if ( !current.canTransitionTo( BookingStatus.CANCELLED ) ) {
      throw new BusinessException( 400, "当前状态[" + current.getDesc() + "]不能取消" );
    }

    // 先把订单从“当前状态”原子改成“已取消”。只有这一步成功，才说明本请求抢到了取消资格。
    int orderAffected = orderMapper.updateStatusIfCurrent(
        orderId,
        current.getCode(),
        BookingStatus.CANCELLED.getCode()
    );
    if ( orderAffected == 0 ) {
      throw new BusinessException( 409, "订单状态已变化，请刷新后重试" );
    }

    int slotAffected = slotMapper.restoreCapacity( order.getSlotId() );
    if ( slotAffected == 0 ) {
      throw new BusinessException( 500, "恢复时段名额失败，请联系管理员" );
    }
  }

  @Override
  public Page < BookingOrderVO > getMyOrders( Long userId, int pageNum, int pageSize ) {
    long safePageNum = Math.max( 1, pageNum );
    long safePageSize = Math.min( MAX_PAGE_SIZE, Math.max( 1, pageSize ) );
    Page < BookingOrderVO > page = new Page <>( safePageNum, safePageSize );
    return orderMapper.selectMyOrders( page, userId );
  }

  @Override
  public Page < BookingOrderAdminVO > getAllOrders( Integer status, String targetType, int pageNum, int PageSize ) {
    // 管理员查看全平台订单，分页兜底逻辑跟getMyOrder完全一致
    long safePageNum = Math.max( 1, pageNum );
    long safePageSize = Math.min( MAX_PAGE_SIZE, Math.max( 1, PageSize ) );
    Page < BookingOrderAdminVO > page = new Page <>( safePageNum, safePageSize );
    return orderMapper.selectAllOrders( page, status, targetType );
  }
}
