// src/main/java/com/travel/vo/CommentVO.java
package com.travel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema ( description = "评论列表VO" )
public class CommentVO {

  @Schema ( description = "评论ID" )
  private Long id;

  @Schema ( description = "评论用户ID" )
  private Long userId;

  @Schema ( description = "发评论的用户昵称" )
  private String nickname;

  @Schema ( description = "景点ID" )
  private Long scenicId;

  @Schema ( description = "景点名称" )
  private String scenicName;

  @Schema ( description = "评论内容" )
  private String content;

  @Schema ( description = "评分 1-5" )
  private Integer rating;

  @Schema ( description = "评论时间" )
  private LocalDateTime createTime;
}
