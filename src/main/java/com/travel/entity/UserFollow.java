package com.travel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName ( "user_follow" )
@Schema ( description = "用户关注关系" )
public class UserFollow {

  @TableId ( type = IdType.AUTO )
  @Schema ( description = "主键ID" )
  private Long id;

  @Schema ( description = "关注者ID（粉丝）" )
  private Long followerId;

  @Schema ( description = "被关注者ID" )
  private Long followingId;

  @Schema ( description = "创建时间" )
  private LocalDateTime createTime;

  @TableLogic
  @Schema ( description = "逻辑删除" )
  private Integer isDeleted;

}
