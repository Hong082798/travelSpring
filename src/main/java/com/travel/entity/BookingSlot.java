package com.travel.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@TableName ( "booking_slot" )
public class BookingSlot {

  @TableId ( type = IdType.AUTO )
  private Long id;

  private String targetType;
  private Long targetId;
  private LocalDate slotDate;

  /**
   * 预约开始时间，对应 booking_slot.start_time。
   * 字段名必须和 DTO/VO 保持一致，否则 BeanUtils.copyProperties 不会拷贝开始时间。
   */
  private LocalTime startTime;

  private LocalTime endTime;
  private Integer capacity;
  private Integer bookedCount;

  /**
   * 0=下架，1=上架。下架只阻止新预订，不会自动取消已有订单。
   */
  private Integer status;

  @TableField ( fill = FieldFill.INSERT )
  private LocalDateTime createTime;

  @TableField ( fill = FieldFill.INSERT_UPDATE )
  private LocalDateTime updateTime;

  @TableLogic
  private Integer isDeleted;
}
