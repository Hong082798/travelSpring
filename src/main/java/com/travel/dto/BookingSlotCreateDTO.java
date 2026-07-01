package com.travel.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 管理员创建预约时段时提交的数据。
 * DTO 只放前端允许填写的字段，bookedCount/status/isDeleted 等系统字段由后端生成。
 */
@Data
public class BookingSlotCreateDTO {

  @NotBlank ( message = "targetType不能为空" )
  private String targetType;

  @NotNull ( message = "targetId不能为空" )
  @Positive ( message = "targetId必须大于0" )
  private Long targetId;

  @NotNull ( message = "预约日期不能为空" )
  @FutureOrPresent ( message = "预约日期不能早于今天" )
  private LocalDate slotDate;

  @NotNull ( message = "开始时间不能为空" )
  private LocalTime startTime;

  @NotNull ( message = "结束时间不能为空" )
  private LocalTime endTime;

  @NotNull ( message = "容量不能为空" )
  @Min ( value = 1, message = "容量至少为1" )
  private Integer capacity;

}
