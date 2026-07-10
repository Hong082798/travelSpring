package com.travel.dto;

import jakarta.validation.constraints.Min;
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

  // 预约份数。@NotNull + @Min(1) 保证前端漏传或传0/负数时，在Controller的参数校验阶段
  // 就直接被Spring Validation拦下并返回400，不会带着非法值进入Service的业务逻辑。
  // 这样Service里对quantity的校验就是"双保险"而非"唯一防线"——前端伪造请求绕过前端校验时，
  // 这里的注解依然生效；而Service里的判断则是防止未来有其他调用方（如内部RPC）跳过Controller直接调用。
  @NotNull ( message = "预约数量不能为空" )
  @Min ( value = 1, message = "预约数量必须大于0" )
  private Integer quantity;

  // 使用的优惠券id，可选，不用券则不传（null）。故意不加@NotNull——
  // "不使用优惠券"是合法的正常下单路径，不是需要校验拦截的异常输入。
  private Long userCouponId;
}
