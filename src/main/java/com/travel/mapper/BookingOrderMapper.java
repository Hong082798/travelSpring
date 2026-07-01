package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.entity.BookingOrder;
import com.travel.vo.BookingOrderAdminVO;
import com.travel.vo.BookingOrderVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface BookingOrderMapper extends BaseMapper < BookingOrder > {

  /**
   * 我的预订列表。
   * 这里用自定义 SQL 是因为 target_type 会分派到不同业务表，单表查询拿不到展示名称。
   */
  Page < BookingOrderVO > selectMyOrders( Page < ? > page, @Param ( "userId" ) Long userId );

  // 管理员查询全平台预约订单列表，不按userId过滤，支持状态关联类型可选筛选
  Page < BookingOrderAdminVO > selectAllOrders( Page < ? > page, @Param ( "status" ) Integer status, @Param (
      "targetType" ) String targetType );

  /**
   * 按“当前状态”做条件更新，避免确认/取消等并发请求互相覆盖。
   * 返回 1 表示状态切换成功，返回 0 表示订单不存在、已删除或状态已被其他请求改走。
   */
  @Update ( "UPDATE booking_order SET status = #{targetStatus}, update_time = NOW() " +
      "WHERE id = #{orderId} AND status = #{currentStatus} AND is_deleted = 0" )
  int updateStatusIfCurrent( @Param ( "orderId" ) Long orderId,
                             @Param ( "currentStatus" ) Integer currentStatus,
                             @Param ( "targetStatus" ) Integer targetStatus );
}
