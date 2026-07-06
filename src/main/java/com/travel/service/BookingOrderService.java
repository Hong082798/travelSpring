package com.travel.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.vo.BookingOrderAdminVO;
import com.travel.vo.BookingOrderVO;

public interface BookingOrderService {
  Long createOrder( Long userId, Long slotId );

  void confirmOrder( Long orderId );

  void cancelOrder( Long userId, Long orderId, boolean isAdmin );

  Page < BookingOrderVO > getMyOrders( Long userId, int pageNum, int pageSize );

  // 管理员查看全平台订单
  Page < BookingOrderAdminVO > getAllOrders( Integer status, String targetType, int pageNum, int PageSize );
}
