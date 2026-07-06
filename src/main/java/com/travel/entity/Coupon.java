package com.travel.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName ( "coupon" )
public class Coupon {

  @TableId ( type = IdType.AUTO )
  private Long id;

  private String couponName;

  // 存的是 CouponType 枚举的 name()，如 "FULL_REDUCTION"/"CASH"。
  private String couponType;

  private BigDecimal discountAmount;

  // 满减门槛。现金券（CASH）该值固定为 0，代表无门槛。
  private BigDecimal thresholdAmount;

  private Integer totalCount;

  // 过期时间，过期后不可使用。已被领取/发放的数量。这是领取环节做并发控制的核心字段
  private Integer receivedCount;

  private Integer perUserLimit;

  private LocalDateTime validStartTime;
  private LocalDateTime validEndTime;

  private Integer status;

  @TableField ( fill = FieldFill.INSERT )
  private LocalDateTime createTime;

  @TableField ( fill = FieldFill.INSERT_UPDATE )
  private LocalDateTime updateTime;

  @TableLogic
  private Integer isDeleted;
}
