package com.travel.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName ( "booking_order" )
public class BookingOrder {

  @TableId ( type = IdType.AUTO )
  private Long id;

  private Long userId;
  private Long slotId;

  /**
   * 订单状态码，取值见 BookingStatus。
   * 实体里存整数，业务层统一通过枚举判断状态流转。
   */
  private Integer status;

  @TableField ( fill = FieldFill.INSERT )
  private LocalDateTime createTime;

  @TableField ( fill = FieldFill.INSERT_UPDATE )
  private LocalDateTime updateTime;

  @TableLogic
  private Integer isDeleted;

}
