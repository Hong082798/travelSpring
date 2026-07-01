package com.travel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travel.service.DashboardService;
import com.travel.service.ScenicSpotService;
import com.travel.service.TravelNoteService;
import com.travel.service.UserService;
import com.travel.vo.DashboardStatsVO;

@Service
public class DashboardServiceImpl implements DashboardService {

  // 注入各个模块的 Service —— 复用它们的 count()，而不是自己写 SQL
    @Autowired
    private UserService userService;
    @Autowired
    private ScenicSpotService scenicSpotService;
    @Autowired
    private TravelNoteService travelNoteService;
    @Autowired
    private CommentService commentService;


    @Override
    public DashboardStatsVO getStats() {

      DashboardStatsVO vo = new DashboardStatsVO();
      // 自动带is_deleted=0的条件，所以只会统计未删除的记录
      vo.setScenicCount(userService.count());
      vo.setScenicCount(scenicSpotService.count());
        
    }
  
}
