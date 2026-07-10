package com.travel.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.travel.common.Result;
import com.travel.dto.AssignCouponDTO;
import com.travel.service.CouponService;
import com.travel.service.UserCouponService;
import com.travel.vo.CouponVO;
import com.travel.vo.UserCouponVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag ( name = "优惠券（用户端）", description = "用户领券、查看可用优惠券及我的优惠券列表" )
@RestController
@RequestMapping ( "/api/coupon" )
public class UserCouponController {

  private final CouponService couponService;
  private final UserCouponService userCouponService;

  public UserCouponController( CouponService couponService, UserCouponService userCouponService ) {
    this.couponService = couponService;
    this.userCouponService = userCouponService;
  }

  @Operation ( summary = "查看可领取的优惠券列表" )
  @GetMapping ( "/available" )
  public Result < List < CouponVO > > listAvailable() {
    return Result.success( couponService.listAvailableCoupons() );
  }

  @Operation ( summary = "领取优惠券" )
  @PostMapping ( "/{couponId}/receive" )
  public Result < Void > receive( @PathVariable Long couponId ) {
    Long userId = StpUtil.getLoginIdAsLong();
    userCouponService.receiveCoupon( userId, couponId );
    return Result.success();
  }

  @Operation ( summary = "查看我的优惠券列表" )
  @GetMapping ( "/my" )
  public Result < List < UserCouponVO > > myCoupons() {
    Long userId = StpUtil.getLoginIdAsLong();
    return Result.success( userCouponService.listMyCoupons( userId ) );
  }

  // 管理员定向发放，单独放在这个Controller里，因为操作的user_coupon
  public Result < Map < Long, String > > assign( @Valid @RequestBody AssignCouponDTO dto ) {
    return Result.success( userCouponService.assignCoupon( dto.getCouponId(), dto.getUserIds() ) );
  }

}
