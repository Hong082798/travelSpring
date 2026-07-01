package com.travel.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema ( description = "评论" )
@TableName ( "comment" )
public class Comment {

  @TableId ( type = IdType.AUTO )
  @Schema ( description = "主键ID" )
  @SuppressWarnings("unused")
  private Long id;

  @Schema ( description = "评论用户ID" )
  private Long userId;

  @Schema ( description = "景点ID" )
  private Long scenicId;

  @Schema ( description = "评论内容" )
  private String content;

  @Schema ( description = "评分" )
  private Integer rating;

  @Schema ( description = "评论时间" )
  private LocalDateTime createTime;

  @Schema ( description = "逻辑删除" )
  private Integer isDeleted;

}
