package com.travel.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 管理员查看全平台预约订单时用的返回结构。
 * 和 BookingOrderVO 的唯一区别是多了 userId/username——
 * 普通用户查“我的订单”不需要知道自己的userId是谁下的，
 * 但管理员看全平台订单必须知道“这是谁下的单”，所以单独建一个VO，
 * 不在 BookingOrderVO 里加这两个字段（避免用户端接口意外泄露其他人的信息结构）。
 */
@Data
public class BookingOrderAdminVO {

  private Long id;
  private Integer status;
  private String statusText;

  // 新增：下单用户信息，普通用户视角的 BookingOrderVO 没有这两个字段
  private Long userId;
  private String username;

  private String targetType;
  private Long targetId;
  private String targetName;
  private LocalDate slotDate;
  private LocalTime startTime;
  private LocalTime endTime;
  private LocalDateTime createTime;

}
