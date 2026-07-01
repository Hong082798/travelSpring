package com.travel.service;

import com.travel.vo.DashboardStatsVO;

public interface DashboardService {
  // 获取Dashboard统计数据
  DashboardStatsVO getStats();  
}
