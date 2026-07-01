package com.travel.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.common.Result;
import com.travel.dto.BookingOrderCreateDTO;
import com.travel.service.BookingOrderService;
import com.travel.vo.BookingOrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag ( name = "预约订单管理", description = "预约订单的创建、确认、取消和我的预约查询" )
@RestController
@RequestMapping ( "/api/booking-orders" )
public class BookingOrderController {

  private final BookingOrderService orderService;

  public BookingOrderController( BookingOrderService orderService ) {
    this.orderService = orderService;
  }

  @SaCheckLogin
  @Operation ( summary = "创建预约订单" )
  @PostMapping
  public Result < Long > createOrder( @Valid @RequestBody BookingOrderCreateDTO dto ) {
    // userId 从登录态取，不能让前端传，避免用户伪造 userId 替别人下单。
    Long userId = StpUtil.getLoginIdAsLong();
    Long orderId = orderService.createOrder( userId, dto.getSlotId() );
    return Result.success( orderId );
  }

  @SaCheckRole ( "admin" )
  @Operation ( summary = "确认预约订单" )
  @PutMapping ( "/{id}/confirm" )
  public Result < Void > confirmOrder( @PathVariable Long id ) {
    orderService.confirmOrder( id );
    return Result.success();
  }

  @SaCheckLogin
  @Operation ( summary = "取消预约订单" )
  @PutMapping ( "/{id}/cancel" )
  public Result < Void > cancelOrder( @PathVariable Long id ) {
    Long userId = StpUtil.getLoginIdAsLong();
    boolean isAdmin = StpUtil.hasRole( "admin" );
    orderService.cancelOrder( userId, id, isAdmin );
    return Result.success();
  }

  @SaCheckLogin
  @Operation ( summary = "查询我的预约订单" )
  @GetMapping ( "/my" )
  public Result < Page < BookingOrderVO > > getMyOrders( @RequestParam ( defaultValue = "1" ) int pageNum,
                                                         @RequestParam ( defaultValue = "10" ) int pageSize ) {
    Long userId = StpUtil.getLoginIdAsLong();
    return Result.success( orderService.getMyOrders( userId, pageNum, pageSize ) );
  }
}
