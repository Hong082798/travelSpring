package com.travel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema ( description = "用户列表项" )
public class UserVO {

  @Schema ( description = "用户ID" )
  private Long id;

  @Schema ( description = "用户名" )
  private String username;

  @Schema ( description = "昵称" )
  private String nickname;

  @Schema ( description = "头像" )
  private String avatar;

  @Schema ( description = "手机号" )
  private String phone;

  @Schema ( description = "性别：0未知，1男，2女" )
  private Integer gender;

  @Schema ( description = "状态：0正常，1禁用" )
  private Integer status;

  @Schema ( description = "注册时间" )
  private LocalDateTime createTime;

  @Schema ( description = "角色列表" )
  private List<String> roles;

}
