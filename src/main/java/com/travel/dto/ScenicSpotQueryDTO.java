package com.travel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema ( description = "景点分页查询参数" )
public class ScenicSpotQueryDTO {

  @Schema ( description = "页码（从1开始）" )
  private Integer pageNum = 1;

  @Schema ( description = "每页数量" )
  private Integer pageSize = 10;

  @Schema ( description = "分类ID（可选，按分类筛选）" )
  private Long categoryId;

  @Schema ( description = "景点名称关键词（可选，模糊搜索）" )
  private String keyword;

}
