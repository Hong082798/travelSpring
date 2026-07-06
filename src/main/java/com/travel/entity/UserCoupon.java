package com.travel.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName ( "user_coupon" )
public class UserCoupon {

  @TableId ( type = IdType.AUTO )
  private Long id;
  private Long couponId;
  private Long userId;

  private Integer status;
  private Long orderId;
  private LocalDateTime receiveTime;
  private LocalDateTime useTime;

  @TableField ( fill = FieldFill.INSERT )
  private LocalDateTime createTime;

  @TableField ( fill = FieldFill.INSERT_UPDATE )
  private LocalDateTime updateTime;

  @TableLogic
  private Integer isDeleted;
}
