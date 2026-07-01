package com.travel.controller;


import cn.dev33.satoken.annotation.SaCheckRole;
import com.travel.common.Result;
import com.travel.service.DashboardService;
import com.travel.vo.CategoryStatsVO;
import com.travel.vo.DashboardStatsVO;
import com.travel.vo.DateStatsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag ( name = "Dashboard接口", description = "管理后台数据" )
@RestController
@RequestMapping ( "/api/dashboard" )
public class DashboardController {

  @Autowired
  private DashboardService dashboardService;

  @Operation ( summary = "获取统计概览", description = "返回总用户数" )
  @SaCheckRole ( "admin" )
  @GetMapping ( "/stats" )
  public Result < DashboardStatsVO > getStats() {
    return Result.success( dashboardService.getStats() );
  }

  @Operation ( summary = "景点分类分布", description = "各分类下的景点数" )
  @SaCheckRole ( "admin" )
  @GetMapping ( "/category-stats" )
  public Result < List < CategoryStatsVO > > getCategoryStats() {
    return Result.success( dashboardService.getCategoryStats() );
  }

  @Operation ( summary = "近7天游记发布趋势", description = "每天的游记发布数" )
  @SaCheckRole ( "admin" )
  @GetMapping ( "/note-trend" )
  public Result < List < DateStatsVO > > getNoteTrend() {
    return Result.success( dashboardService.getNoteTrend() );
  }

}
