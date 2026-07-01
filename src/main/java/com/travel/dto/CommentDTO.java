package com.travel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema ( description = "评论请求参数" )
public class CommentDTO {

  @NotBlank ( message = "评论内容不能为空" )
  @Schema ( description = "评论内容", example = "这个景点太棒了！" )
  private String content;

  @NotNull ( message = "评分不能为空" )
  @Min ( value = 1, message = "评分必须大于等于1" )
  @Max ( value = 5, message = "评分最高5星" )
  @Schema ( description = "评分，1-5星", example = "5" )
  private Integer rating;

}
