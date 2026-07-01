package com.travel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.travel.service.DashboardService;
import com.travel.service.UserService;
import com.travel.vo.DashboardStatsVO;

public class DashboardServiceImpl implements DashboardService {

  // 注册各个模块的Service
  @Autowired
  private UserService userService;
  @Autowired
  private 


    @Override
    public DashboardStatsVO getStats() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
  
}
