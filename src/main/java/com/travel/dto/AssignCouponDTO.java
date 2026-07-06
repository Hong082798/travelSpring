package com.travel.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AssignCouponDTO {

  @NotNull ( message = "优惠卷ID不能为空！" )
  private Long couponId;

  // 管理员定向发放的目标用户ID列表，支持一次发给多个人
  @NotEmpty ( message = "目标用户ID列表不能为空！" )
  private List < Long > userIds;

}
