package com.travel.service;

import com.travel.vo.UserCouponVO;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface UserCouponService {

  // 用户主动领取优惠卷
  void receiveCoupon( Long userId, Long couponId );

  // 管理员定向发放，返回值是每个用户id对应的发放结果
  Map < Long, String > assignCoupon( Long couponId, @NotEmpty List < Long > userIds );

  // 查询我的优惠卷列表
  List < UserCouponVO > listMyCoupons( Long userId );

  // 返回实际抵扣的金额
  BigDecimal validateAndCalculateDiscount( Long userId, Long userCouponId, BigDecimal orderOriginalAmount );

  // 必须和订单创建在同一个事物里调用
  void markAsUsed( Long userCouponId, Long orderId );

}
