package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.entity.BookingSlot;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface BookingSlotMapper extends BaseMapper < BookingSlot > {

  /**
   * 原子扣名额：容量判断和 booked_count + 1 在同一条 UPDATE 里完成。
   * MySQL 会对命中的行加锁，所以高并发下也只会有 capacity 次更新成功。
   *
   * 返回值：1=扣减成功；0=时段不存在、已满、已下架、已删除或已经开始。
   */
  @Update ( "UPDATE booking_slot SET booked_count = booked_count + 1 " +
      "WHERE id = #{slotId} AND booked_count < capacity AND status = 1 AND is_deleted = 0 " +
      "AND (slot_date > CURRENT_DATE OR (slot_date = CURRENT_DATE AND start_time > CURRENT_TIME))" )
  int deductCapacity( @Param ( "slotId" ) Long slotId );

  /**
   * 取消订单时恢复名额。
   * booked_count > 0 是兜底保护，真正避免重复恢复靠订单状态的条件更新。
   */
  @Update ( "UPDATE booking_slot SET booked_count = booked_count - 1 " +
      "WHERE id = #{slotId} AND booked_count > 0 AND is_deleted = 0" )
  int restoreCapacity( @Param ( "slotId" ) Long slotId );
}
