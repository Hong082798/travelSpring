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
  @Deprecated
  private CommentServiceImpl commentService; // 评论模块暂时不统计了，后续如果要加缓存，这里就有意义了


    @Override
    public DashboardStatsVO getStats() {

      DashboardStatsVO vo = new DashboardStatsVO();
      
        
    }
  
}
