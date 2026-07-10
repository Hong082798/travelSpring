package com.travel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.travel.entity.Coupon;
import com.travel.entity.UserCoupon;
import com.travel.enums.CouponType;
import com.travel.enums.UserCouponStatus;
import com.travel.mapper.CouponMapper;
import com.travel.mapper.UserCouponMapper;
import com.travel.service.UserCouponService;
import com.travel.vo.UserCouponVO;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserCouponServiceImpl implements UserCouponService {

  private final CouponMapper couponMapper;
  private final UserCouponMapper userCouponMapper;

  public UserCouponServiceImpl( CouponMapper couponMapper, UserCouponMapper userCouponMapper ) {
    this.couponMapper = couponMapper;
    this.userCouponMapper = userCouponMapper;
  }

  @Override
  @Transactional
  public void receiveCoupon( Long userId, Long couponId ) {

    Coupon coupon = couponMapper.selectById( couponId );
    if ( coupon == null || coupon.getStatus() == 0 ) {
      throw new RuntimeException( "优惠券不存在或已下架" );
    }

    LocalDateTime now = LocalDateTime.now();
    if ( now.isBefore( coupon.getValidStartTime() ) || now.isAfter( coupon.getValidEndTime() ) ) {
      throw new RuntimeException( "优惠券不在领取有效期内" );
    }

    // 每个人限领校验，必须在扣库存之前做，如果反过来，会出现库存已经扣了，但才发现这个用户领超了
    long alreadyReceived = userCouponMapper.selectCount(
        new LambdaQueryWrapper < UserCoupon >()
            .eq( UserCoupon :: getUserId, userId )
            .eq( UserCoupon :: getCouponId, couponId )
    );
    if ( alreadyReceived >= coupon.getPerUserLimit() ) {
      throw new RuntimeException( "每人限领" + coupon.getPerUserLimit() + "张" );
    }

    // 核心并发控制点，条件更新，返回0说明库存被人抢完了
    int affected = couponMapper.incrementReceivedCount( couponId );
    if ( affected == 0 ) {
      throw new RuntimeException( "优惠券已被抢完" );
    }

    UserCoupon userCoupon = new UserCoupon();
    userCoupon.setCouponId( couponId );
    userCoupon.setUserId( userId );
    userCoupon.setStatus( 0 ); // 0表示未使用，1表示已
    userCoupon.setReceiveTime( LocalDateTime.now() );
    userCouponMapper.insert( userCoupon );

  }

  @Override
  @Transactional
  public Map < Long, String > assignCoupon( Long couponId, @NotEmpty List < Long > userIds ) {

    Coupon coupon = couponMapper.selectById( couponId );
    if ( coupon == null ) {
      throw new RuntimeException( "优惠券不存在" );
    }

    Map < Long, String > result = new HashMap <>();
    for ( Long userId : userIds ) {
      // 定向发放场景管理员操作，不存在真是并发竞争
      long alreadyReceived = userCouponMapper.selectCount(
          new LambdaQueryWrapper < UserCoupon >()
              .eq( UserCoupon :: getUserId, userId )
              .eq( UserCoupon :: getCouponId, couponId )
      );
      if ( alreadyReceived >= coupon.getPerUserLimit() ) {
        result.put( userId, "每人限领" + coupon.getPerUserLimit() + "张" );
        continue;
      }

      int affected = couponMapper.incrementReceivedCount( couponId );
      if ( affected == 0 ) {
        result.put( userId, "优惠券已被抢完" );
        continue;
      }

      UserCoupon userCoupon = new UserCoupon();
      userCoupon.setCouponId( couponId );
      userCoupon.setUserId( userId );
      userCoupon.setStatus( 0 ); // 0表示未使用，1表示已
      userCoupon.setReceiveTime( LocalDateTime.now() );
      userCouponMapper.insert( userCoupon );
      result.put( userId, "发放成功" );
    }

    return result;
  }

  @Override
  public List < UserCouponVO > listMyCoupons( Long userId ) {

    List < UserCoupon > list = userCouponMapper.selectList(
        new LambdaQueryWrapper < UserCoupon >()
            .eq( UserCoupon :: getUserId, userId )
    );

    return list.stream().map( this :: toVO ).collect( Collectors.toList() );
  }

  private UserCouponVO toVO( UserCoupon userCoupon ) {

    UserCouponVO vo = new UserCouponVO();
    BeanUtils.copyProperties( userCoupon, vo );
    Coupon coupon = couponMapper.selectById( userCoupon.getCouponId() );
    vo.setCouponName( coupon.getCouponName() );
    vo.setCouponType( coupon.getCouponType() );
    vo.setCouponTypeDesc( CouponType.fromValue( coupon.getCouponType() ).getDesc() );
    vo.setDiscountAmount( coupon.getDiscountAmount() );
    vo.setThresholdAmount( coupon.getThresholdAmount() );
    vo.setValidEndTime( coupon.getValidEndTime() );
    vo.setStatusDesc( UserCouponStatus.fromCode( userCoupon.getStatus() ).getDesc() );
    return vo;

  }

  @Override
  public BigDecimal validateAndCalculateDiscount( Long userId, Long userCouponId, BigDecimal orderOriginalAmount ) {

    UserCoupon userCoupon = userCouponMapper.selectById( userCouponId );
    if ( userCoupon == null || !userCoupon.getUserId().equals( userId ) ) {
      throw new RuntimeException( "优惠券不存在，或当前优惠卷不属于当前用户" );
    }
    if ( UserCouponStatus.fromCode( userCoupon.getStatus() ) != UserCouponStatus.UNUSED ) {
      throw new RuntimeException( "优惠券已使用或已过期，不能再次使用" );
    }

    Coupon coupon = couponMapper.selectById( userCoupon.getCouponId() );
    if ( LocalDateTime.now().isAfter( coupon.getValidEndTime() ) ) {
      throw new RuntimeException( "优惠券已过期，不能使用" );
    }

    CouponType type = CouponType.fromValue( coupon.getCouponType() );
    if ( type == CouponType.FULL_REDUCTION && orderOriginalAmount.compareTo( coupon.getThresholdAmount() ) < 0 ) {
      throw new RuntimeException( "订单金额未达到满减门槛，不能使用该优惠券" );
    }

    return coupon.getDiscountAmount();
  }

  @Override
  public void markAsUsed( Long userCouponId, Long orderId ) {

    UserCoupon userCoupon = userCouponMapper.selectById( userCouponId );
    userCoupon.setStatus( UserCouponStatus.USED.getCode() );
    userCoupon.setOrderId( orderId );
    userCoupon.setUseTime( LocalDateTime.now() );
    userCouponMapper.updateById( userCoupon );

  }
}
