package com.travel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema ( description = "游记发布/编辑DTO" )
public class TravelNoteDTO {

  @Schema ( description = "游记ID，编辑时必传，发布时不传" )
  private Long id;

  @NotBlank ( message = "标题不能为空" )
  @Schema ( description = "游记标题" )
  private String title;

  @NotBlank ( message = "内容不能为空" )
  @Schema ( description = "游记正文" )
  private String content;

  @Schema ( description = "封面图片URL，可选" )
  private String coverImage;

  @Schema ( description = "关联景点ID，可选" )
  private Long scenicId;

  @NotNull ( message = "状态不能为空" )
  @Schema ( description = "状态：1=发布 0=存草稿" )
  private Integer status;

}
