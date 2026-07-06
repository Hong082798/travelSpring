package com.travel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.dto.CouponCreateDTO;
import com.travel.entity.Coupon;
import com.travel.enums.CouponType;
import com.travel.mapper.CouponMapper;
import com.travel.service.CouponService;
import com.travel.vo.CouponVO;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CouponServiceImpl implements CouponService {

  private final CouponMapper couponMapper;

  public CouponServiceImpl( CouponMapper couponMapper ) {
    this.couponMapper = couponMapper;
  }

  @Override
  public Long createCoupon( CouponCreateDTO dto ) {

    CouponType type = CouponType.fromValue( dto.getCouponType() );
    // 满减券必须有大于0的门槛，现金券门槛固定为0——这是两种类型唯一的行为差异点
    if ( type == CouponType.FULL_REDUCTION
        && ( dto.getThresholdAmount() == null || dto.getThresholdAmount().signum() <= 0 ) ) {
      throw new RuntimeException( "满减券必须设置大于0的满减门槛" );
    }

    Coupon coupon = new Coupon();
    BeanUtils.copyProperties( dto, coupon );
    if ( type == CouponType.CASH ) {
      coupon.setThresholdAmount( BigDecimal.ZERO );
    }
    coupon.setReceivedCount( 0 );
    coupon.setStatus( 1 ); // 1表示上架中，0表示已下架

    couponMapper.insert( coupon );

    return coupon.getId();
  }

  @Override
  public void offlineCoupon( Long couponId ) {

    Coupon coupon = couponMapper.selectById( couponId );
    if ( coupon == null ) {
      throw new RuntimeException( "优惠券不存在" );
    }
    coupon.setStatus( 0 ); // 0表示已下架
    couponMapper.updateById( coupon );

  }

  @Override
  public Page < CouponVO > pageCoupons( int pageNum, int pageSize ) {

    Page < Coupon > page = couponMapper.selectPage( new Page <>( pageNum, pageSize ), null );
    Page < CouponVO > voPage = new Page <>( pageNum, pageSize, page.getTotal() );
    voPage.setRecords( page.getRecords().stream().map( this :: toVO ).collect( Collectors.toList() ) );

    return voPage;
  }

  @Override
  public List < CouponVO > listAvailableCoupons() {

    LocalDateTime now = LocalDateTime.now();
    LambdaQueryWrapper < Coupon > wrapper = new LambdaQueryWrapper < Coupon >()
        .eq( Coupon :: getStatus, 1 )
        .le( Coupon :: getValidStartTime, now )
        .ge( Coupon :: getValidEndTime, now )
        .apply( "received_count < total_count" );

    return couponMapper.selectList( wrapper ).stream().map( this :: toVO ).collect( Collectors.toList() );
  }

  private CouponVO toVO( Coupon coupon ) {
    CouponVO vo = new CouponVO();
    BeanUtils.copyProperties( coupon, vo );
    CouponType type = CouponType.fromValue( coupon.getCouponType() );
    vo.setCouponTypeDesc( type.getDesc() );
    vo.setReceivedCount( coupon.getTotalCount() - coupon.getReceivedCount() );
    return vo;
  }

}
