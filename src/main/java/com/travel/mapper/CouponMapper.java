package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.entity.Coupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CouponMapper extends BaseMapper < Coupon > {

  // 领取优惠卷的原子扣减
  int incrementReceivedCount( @Param ( "couponId" ) Long couponId );

}
