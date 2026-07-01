package com.travel.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class BookingOrderVO {
  private Long id;
  private Integer status;
  // 给前端直接展示的状态文案，避免每个端都维护一份状态码映射。
  private String statusText;
  private String targetType;
  private Long targetId;
  // 通过 targetType 分派联表得到的景点名或玩乐项目名。
  private String targetName;
  private LocalDate slotDate;
  private LocalTime startTime;
  private LocalTime endTime;
  private LocalDateTime createTime;
}
