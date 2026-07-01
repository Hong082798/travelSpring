package com.travel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travel.service.DashboardService;
import com.travel.vo.DashboardStatsVO;

@Service
public class DashboardServiceImpl implements DashboardService {

  // 注册各个模块
  @Autowired
  private UserServiceImpl userService;
  @Autowired
  private ScenicServiceImpl scenicService;
  @Autowired
  private TravelServiceImpl travelService;
  @


    @Override
    public DashboardStatsVO getStats() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
  
}
