package com.travel.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.vo.BookingOrderAdminVO;
import com.travel.vo.BookingOrderVO;

public interface BookingOrderService {
  /**
   * 【修改】新增 quantity（预约份数）和 userCouponId（可选，使用的优惠券id，不用则传null）两个参数。
   * 旧签名 createOrder(Long userId, Long slotId) 的调用方（Controller）也要同步改，
   * 这个我没有你的BookingOrderController代码，改完这批你需要自己同步一下入参，
   * 或者把Controller也发我，我帮你对齐。
   */
  Long createOrder( Long userId, Long slotId, Integer quantity, Long userCouponId );

  void confirmOrder( Long orderId );

  void cancelOrder( Long userId, Long orderId, boolean isAdmin );

  Page < BookingOrderVO > getMyOrders( Long userId, int pageNum, int pageSize );

  Page < BookingOrderAdminVO > getAllOrders( Integer status, String targetType, int pageNum, int PageSize );
}
