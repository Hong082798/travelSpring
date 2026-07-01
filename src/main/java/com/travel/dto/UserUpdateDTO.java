package com.travel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema ( description = "修改个人资料请求参数" )
public class UserUpdateDTO {

  @Size ( max = 50, message = "昵称长度不能超过50个字符" )
  @Schema ( description = "昵称", example = "旅行者123" )
  private String nickname;

  @Schema ( description = "头像URL" )
  private String avatar;

  @Size ( max = 20, message = "电话号码长度不能超过20个字符" )
  @Schema ( description = "手机号" )
  private String phone;

  @Schema ( description = "性别（0-未知，1-男，2-女）" )
  private Integer gender;

}
