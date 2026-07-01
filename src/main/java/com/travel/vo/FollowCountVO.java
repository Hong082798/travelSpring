package com.travel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema ( description = "关注数量统计" )
public class FollowCountVO {

  @Schema ( description = "关注数（我关注饿了多少人）" )
  private Long followingCount;

  @Schema ( description = "粉丝数（多少人关注我）" )
  private Long followCount;

}
