package com.travel.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.dto.CouponCreateDTO;
import com.travel.vo.CouponVO;

import java.util.List;

public interface CouponService {

  // 管理员管理优惠卷
  Long createCoupon( CouponCreateDTO dto );

  // 管理员下架优惠卷(不影响用户手里现有的优惠卷)
  void offlineCoupon( Long couponId );

  // 管理端分页查看所有优惠卷模板(包含已经下架的)
  Page < CouponVO > pageCoupons( int pageNum, int pageSize );

  // 用户端领取优惠卷列表，只返回上架中，且有效期内的
  List < CouponVO > listAvailableCoupons();

}
