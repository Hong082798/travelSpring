package com.travel.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserCouponVO {

  private Long id;
  private Long couponId;
  private String couponName;
  private String couponType;
  private String couponTypeDesc;
  private BigDecimal discountAmount;
  private BigDecimal thresholdAmount;

  private Integer status;
  private String statusDesc;

  private Long orderId;
  private LocalDateTime receiveTime;
  private LocalDateTime useTime;
  private LocalDateTime validEndTime;
}
