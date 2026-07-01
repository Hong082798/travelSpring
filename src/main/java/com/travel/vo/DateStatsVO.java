package com.travel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema ( description = "按日期统计" )
public class DateStatsVO {

  @Schema ( description = "日期格式" )
  private String date;

  @Schema ( description = "当天数量" )
  private Long count;

}
