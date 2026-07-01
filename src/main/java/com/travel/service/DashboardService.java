package com.travel.service;

import com.travel.vo.CategoryStatsVO;
import com.travel.vo.DashboardStatsVO;
import com.travel.vo.DateStatsVO;

import java.util.List;
;

public interface DashboardService {
  // 获取Dashboard统计数据
  DashboardStatsVO getStats();

  List < CategoryStatsVO > getCategoryStats();

  List < DateStatsVO > getNoteTrend();
}
