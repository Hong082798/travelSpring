package com.travel.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.common.Result;
import com.travel.dto.CouponCreateDTO;
import com.travel.service.CouponService;
import com.travel.vo.CouponVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag ( name = "优惠券管理（管理员）", description = "管理员对优惠券的创建、下线和分页查询" )
@RestController
@RequestMapping ( "/api/admin/coupon" )
@SaCheckRole ( "admin" )
public class CouponController {

  private final CouponService couponService;

  public CouponController( CouponService couponService ) {
    this.couponService = couponService;
  }

  @Operation ( summary = "创建优惠券" )
  @PostMapping
  public Result < Long > create( @Valid @RequestBody CouponCreateDTO dto ) {
    return Result.success( couponService.createCoupon( dto ) );
  }

  @Operation ( summary = "下线优惠券" )
  @PutMapping ( "/{id}/offline" )
  public Result < Void > offline( @PathVariable Long id ) {
    couponService.offlineCoupon( id );
    return Result.success();
  }

  @Operation ( summary = "分页查询优惠券列表" )
  @GetMapping
  public Result < Page < CouponVO > > page(
      @RequestParam ( defaultValue = "1" ) int pageNum,
      @RequestParam ( defaultValue = "10" ) int pageSize
  ) {
    return Result.success( couponService.pageCoupons( pageNum, pageSize ) );
  }

}
