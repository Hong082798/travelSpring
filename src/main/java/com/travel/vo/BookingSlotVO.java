package com.travel.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookingSlotVO {
  private Long id;
  private String targetType;
  private Long targetId;
  private LocalDate slotDate;
  private LocalTime startTime;
  private LocalTime endTime;
  private Integer capacity;
  private Integer bookedCount;
  // 计算字段：capacity - bookedCount。前端直接展示，不再重复计算。
  private Integer remainingCount;
  private Integer status;
}
