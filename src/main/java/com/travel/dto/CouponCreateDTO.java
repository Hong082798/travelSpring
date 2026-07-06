package com.travel.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import javax.xml.stream.XMLEventWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponCreateDTO {

  @NotBlank ( message = "优惠券名称不能为空" )
  private String couponName;

  /**
   * 前端传字符串（"FULL_REDUCTION"/"CASH"），Controller接收后
   * 用 CouponType.fromValue() 校验并转换——这里不用 @Pattern 卡枚举值，
   * 因为 fromValue() 已经能给出更明确的业务报错信息，重复校验没有必要。
   */
  @NotBlank ( message = "优惠券类型不能为空" )
  private String couponType;

  @NotNull ( message = "优惠券金额不能为空" )
  @DecimalMin ( value = "0.0", inclusive = false, message = "优惠券金额必须大于0" )
  private BigDecimal discountAmount;

  /**
   * 满减门槛。现金券（CASH）由前端传 0，不在这里用 @NotNull 强制，
   * 因为两种类型的合法取值不同，交给 Service 层按 couponType 分支校验更准确。
   */
  private BigDecimal thresholdAmount;

  @NotNull ( message = "发放总量不能为空" )
  @Min ( value = 1, message = "发放总量必须大于0" )
  private Integer totalCount;

  @NotNull ( message = "每个人限领张数不能为空" )
  @Min ( value = 1, message = "每个人限领张数必须大于0" )
  private Integer perUserLimit;

  @NotNull ( message = "生效开始时间不能为空" )
  private LocalDateTime validStartTime;

  @NotNull ( message = "生效结束时间不能为空" )
  private LocalDateTime validEndTime;


}
