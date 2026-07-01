package com.travel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema ( description = "游记展示对象" )
public class TravelNoteVO {

  private Long id;
  private Long userId;

  @Schema ( description = "作者昵称" )
  private String authorNickname;

  @Schema ( description = "作者头像" )
  private String authorAvatar;

  private String title;
  private String content;
  private String coverImage;
  private Long scenicId;
  private Integer status;
  private Integer likeCount;
  private Integer viewCount;

  @Schema ( description = "当前用户是否已点赞" )
  private Boolean liked;

  private LocalDateTime createTime;

}
