package com.travel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema ( description = "用户列表查询条件" )
public class UserQueryDTO {

  @Schema ( description = "页码，默认1" )
  private Integer pageNum = 1;

  @Schema ( description = "每页数量，默认10" )
  private Integer pageSize = 10;

  @Schema ( description = "关键词，模糊匹配用户名或昵称" )
  private String keyword;

  @Schema ( description = "状态筛选：0=正常，1=禁用，不传查全部" )
  private Integer status;

}
