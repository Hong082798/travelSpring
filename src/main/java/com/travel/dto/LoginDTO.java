package com.travel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema ( description = "登录请求数据传输对象，包含用户登录所需的基本信息" )
public class LoginDTO {

  @NotBlank ( message = "用户名不能为空" )
  @Schema ( description = "登录账号" )
  private String username;

  @NotBlank ( message = "密码不能为空" )
  @Schema ( description = "登录密码" )
  private String password;

}
