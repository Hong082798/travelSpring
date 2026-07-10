package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.entity.BookingSlot;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface BookingSlotMapper extends BaseMapper < BookingSlot > {

  /**
   * 原子扣名额：容量判断和 booked_count 的增加在同一条 UPDATE 语句里完成。
   * <p>
   * 为什么要写成一条 UPDATE，而不是"先 SELECT 判断名额，再 UPDATE 扣减"两步？
   * 因为两步之间存在时间窗口：假设名额只剩 1 份，两个用户同时 SELECT 到
   * booked_count=4 < capacity=5，都以为自己能订，然后都执行 UPDATE，
   * 最终会超卖。而这里把"判断条件"直接写进 UPDATE 的 WHERE 里，
   * MySQL 对命中的行加锁后才会执行更新，同一时刻只有一个事务能改成功，
   * 天然避免了超卖，不需要额外加悲观锁或分布式锁。
   * <p>
   * 判断条件从"booked_count < capacity"（是否还有名额）改成
   * "booked_count + #{quantity} <= capacity"（剩余名额是否够这次要的份数），
   * 这是支持"一次预约多份"后唯一必须变化的语义，其余状态/日期条件不变。
   *
   * @return 1=扣减成功；0=时段不存在、名额不足、已下架、已删除或已经开始（不会抛异常，由调用方判断返回值）
   */
  @Update ( "UPDATE booking_slot SET booked_count = booked_count + #{quantity} " +
      "WHERE id = #{slotId} AND booked_count + #{quantity} <= capacity AND status = 1 AND is_deleted = 0 " +
      "AND (slot_date > CURRENT_DATE OR (slot_date = CURRENT_DATE AND start_time > CURRENT_TIME))" )
  int deductCapacity( @Param ( "slotId" ) Long slotId, @Param ( "quantity" ) Integer quantity );

  /**
   * 取消订单时恢复名额，恢复的份数必须等于当初这张订单扣掉的 quantity，
   * 而不是固定减 1——否则一份订多张票的订单取消后，名额只会少恢复。
   * <p>
   * WHERE 里的 "booked_count >= #{quantity}" 是防止扣成负数的兜底：
   * 正常业务流程下 booked_count 不可能小于自己订单占用的份数，
   * 但如果没有这个兜底，一旦出现数据异常，UPDATE 会把 booked_count
   * 减成负数——这种脏数据不会立刻报错，只会在后续的名额判断里
   * 悄悄导致"明明没满却拒绝所有预约"这类难排查的问题，所以宁可
   * 这条 UPDATE 影响行数为 0（调用方会感知到并报错），也不让数据变脏。
   * 真正防止"同一订单被重复取消、重复恢复名额"的，是 Service 层
   * 对订单状态做的条件更新（updateStatusIfCurrent），这里的条件只是兜底。
   *
   * @return 1=恢复成功；0=时段不存在、已删除，或 booked_count 异常小于 quantity
   */
  @Update ( "UPDATE booking_slot SET booked_count = booked_count - #{quantity} " +
      "WHERE id = #{slotId} AND booked_count >= #{quantity} AND is_deleted = 0" )
  int restoreCapacity( @Param ( "slotId" ) Long slotId, @Param ( "quantity" ) Integer quantity );
}
