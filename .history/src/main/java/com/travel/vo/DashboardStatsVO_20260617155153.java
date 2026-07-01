package com.travel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DashboardStatsVO，包含仪表盘统计数据的视图对象")
public class DashboardStatsVO {

  @Schema(description = "总用户数", example = "1000")
  private Long totalCount;

  
}
