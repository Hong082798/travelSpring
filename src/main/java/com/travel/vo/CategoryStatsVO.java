package com.travel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema ( description = "景点分类统计项" )
public class CategoryStatsVO {

  @Schema ( description = "分类名称" )
  private String categoryName;

  @Schema ( description = "该分类下的景点数量" )
  private Long count;

}
