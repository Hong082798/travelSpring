package com.travel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema ( description = "游记列表查询条件" )
public class TravelNoteQueryDTO {

  @Schema ( description = "页码，默认第1页" )
  private Integer pageNum = 1;

  @Schema ( description = "每页数量，默认10条" )
  private Integer pageSize = 10;

  @Schema ( description = "关键词，模糊匹配标题" )
  private String keyword;

  @Schema ( description = "关联景点ID，按景点筛选" )
  private Long scenicId;

  @Schema ( description = "作者用户ID，查某人的游记" )
  private Long userId;

}
