package com.travel.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponVO {

  private Long id;
  private String couponName;
  private String couponType;

  // 类型的中文展示文案，直接取CouponType.getDesc()
  private String couponTypeDesc;

  private BigDecimal discountAmount;
  private BigDecimal thresholdAmount;
  private Integer totalCount;
  private Integer receivedCount;

  // 剩余可领取数量=totalCount - receivedCount,在Service里算好直接给前端
  private Integer remainingCount;

  private Integer perUserLimit;
  private LocalDateTime validStartTime;
  private LocalDateTime validEndTime;
  private Integer status;

}
