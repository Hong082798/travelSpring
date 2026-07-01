package com.travel.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.travel.common.Result;
import com.travel.dto.BookingSlotCreateDTO;
import com.travel.service.BookingSlotService;
import com.travel.vo.BookingSlotVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag ( name = "预约时段管理", description = "预约时段的创建、查询和下架" )
@RestController
@RequestMapping ( "/api/booking-slots" )
public class BookingSlotController {

  private final BookingSlotService slotService;

  public BookingSlotController( BookingSlotService slotService ) {
    this.slotService = slotService;
  }

  @SaCheckRole ( "admin" )   // 只有管理员能配置时段
  @Operation ( summary = "创建预约时段" )
  @PostMapping
  public Result < Long > createSlot( @Valid @RequestBody BookingSlotCreateDTO dto ) {
    Long slotId = slotService.createSlot( dto );
    return Result.success( slotId );
  }

  @SaCheckLogin   // 普通登录用户就能查可预约时段
  @Operation ( summary = "查询可预约时段" )
  @GetMapping ( "/available" )
  public Result < List < BookingSlotVO > > listAvailable( @RequestParam ( required = false ) String targetType,
                                                          @RequestParam ( required = false ) Long targetId ) {
    return Result.success( slotService.listAvailableSlots( targetType, targetId ) );
  }

  @SaCheckRole ( "admin" )
  @Operation ( summary = "下架预约时段" )
  @PutMapping ( "/{id}/disable" )
  public Result < Void > disableSlot( @PathVariable Long id ) {
    slotService.disableSlot( id );
    return Result.success();
  }
}
