package com.travel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema ( description = "注册请求数据传输对象，包含用户注册所需的基本信息" )
public class RegisterDTO {

  @NotBlank ( message = "用户名不能为空" )
  @Size ( min = 4, max = 20, message = "用户名长度必须在4到20个字符之间" )
  @Schema ( description = "登录账号" )
  private String username;

  @NotBlank ( message = "密码不能为空" )
  @Size ( min = 6, max = 20, message = "密码长度必须在6到20个字符之间" )
  @Schema ( description = "登录密码" )
  private String password;

  @Schema ( description = "昵称" )
  private String nickname;

  @Schema ( description = "手机号" )
  private String phone;

}
