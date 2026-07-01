package com.travel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema ( description = "关注/粉丝列表项" )
public class UserFollowVO {

  @Schema ( description = "对方用户ID" )
  private Long userId;

  @Schema ( description = "对方昵称" )
  private String nickname;

  @Schema ( description = "对方头像" )
  private String avatar;

  @Schema ( description = "关注建立时间" )
  private LocalDateTime createTime;

}
