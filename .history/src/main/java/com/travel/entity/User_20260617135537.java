package com.travel.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName ( "user" )
@Schema ( description = "用户实体类，包含用户的基本信息" )
public class User {

  @TableId ( type = IdType.AUTO )
  @Schema ( description = "用户ID，主键，自增", example = "1" )
  private Long id;

  @Schema ( description = "用户名，唯一", example = "john_doe" )
  private String username;

  @Schema ( description = "用户密码，存储时应加密", example = "encrypted_password" )
  @JsonIgnore // 序列化JSON时，自动跳过该字段，前端永远收不到password字段，避免密码泄露
  private String password;

  @Schema ( description = "用户昵称", example = "John" )
  private String nickname;

  @Schema ( description = "用户头像URL", example = "http://example.com/avatar.jpg" )
  private String avatar;

  @Schema ( description = "用户电话", example = "13800138000" )
  private String phone;

  @Schema ( description = "用户状态，0表示正常，1表示禁用", example = "0" )
  private Integer status;

  @Schema ( description = "用户性别，0表示未知，1表示男性，2表示女性", example = "1" )
  private Integer gender;

  @Schema ( description = "用户创建时间", example = "2024-06-01T12:00:00" )
  private LocalDateTime createTime;

  @Schema ( description = "用户更新时间", example = "2024-06-01T12:00:00" )
  private LocalDateTime updateTime;

  @Schema ( description = "逻辑删除标志，0表示未删除，1表示已删除", example = "0" )
  @JsonIgnore 
  private Integer isDeleted;

}
