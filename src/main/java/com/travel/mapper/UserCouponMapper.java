package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.entity.UserCoupon;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserCouponMapper extends BaseMapper < UserCoupon > {

  /**
   * 目前没有自定义SQL——统计"用户领了几张同种券"、"我的优惠券列表"这些查询
   * 都能靠 BaseMapper 自带的 selectCount/selectList 配合 LambdaQueryWrapper 完成，
   * 不需要在这里手写方法。之所以还单独建这个文件而不是省略，是因为 MyBatis-Plus
   * 的 Service 层约定依赖每张表都有一个对应 Mapper，属于框架的固定套路，不是可选项。
   */

}
