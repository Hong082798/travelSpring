package com.travel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DashboardStatsVO，包含仪表盘统计数据的视图对象")
public class DashboardStatsVO {

  @Schema(description = "总用户数", example = "1000")
  private Long totalUsers;

  @Schema(description = "总订单数", example = "500")
  private Long totalOrders;

  @Schema(description = "总收入", example = "100000.00")
  private Double totalRevenue;
  
}
