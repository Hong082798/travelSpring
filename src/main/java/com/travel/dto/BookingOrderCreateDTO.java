package com.travel.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户创建预约订单时提交的数据。
 * userId 不从前端传，Controller 会从 Sa-Token 登录态读取，避免用户伪造身份代下单。
 */
@Data
public class BookingOrderCreateDTO {

  @NotNull ( message = "slotId不能为空" )
  private Long slotId;
}
