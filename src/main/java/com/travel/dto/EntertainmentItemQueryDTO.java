package com.travel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema ( description = "玩乐项目分页查询参数" )
public class EntertainmentItemQueryDTO {

  @Schema ( description = "页码，从1开始" )
  private Integer pageNum = 1;

  @Schema ( description = "每页数量" )
  private Integer pageSize = 10;

  @Schema ( description = "分类ID，可选" )
  private Long categoryId;

  @Schema ( description = "关键词，可按照名称模糊搜索" )
  private String keyword;

  @Schema ( description = "状态：0-下架，1-上架可选" )
  private Integer status;

}
