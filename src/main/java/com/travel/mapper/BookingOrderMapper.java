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
   * 按"当前状态"做条件更新，避免确认/取消等并发请求互相覆盖。
   * <p>
   * 为什么要在 WHERE 里带上 currentStatus，而不是"先 selectById 判断状态，
   * 再单独 UPDATE"？因为如果拆成两步，两个并发请求可能都读到同一个旧状态
   * （比如都读到"待确认"），然后都各自认为自己可以转换成功，其中一个的
   * 业务判断结果实际上已经过期了。把 currentStatus 塞进 WHERE 条件，
   * 相当于把"读状态"和"改状态"合并成一次原子操作：谁先执行谁成功，
   * 后到的请求会因为这一行已经不满足 status = #{currentStatus} 而
   * 影响 0 行，Service 层据此判断"状态已被别人改走，请求需要重试或报错"，
   * 而不会出现两个请求都以为自己成功、状态被错误地二次流转的情况。
   *
   * @return 1=状态切换成功；0=订单不存在、已删除，或状态已被其他请求改走
   */
  @Update ( "UPDATE booking_order SET status = #{targetStatus}, update_time = NOW() " +
      "WHERE id = #{orderId} AND status = #{currentStatus} AND is_deleted = 0" )
  int updateStatusIfCurrent( @Param ( "orderId" ) Long orderId,
                             @Param ( "currentStatus" ) Integer currentStatus,
                             @Param ( "targetStatus" ) Integer targetStatus );
}
