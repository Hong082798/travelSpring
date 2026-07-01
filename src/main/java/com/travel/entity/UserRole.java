package com.travel.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema ( description = "user_role" )
public class UserRole {

  @TableId ( type = IdType.AUTO )
  private Long id;

  private Long userId; // 映射user_id
  private Long roleId; // 映射role_id

  @TableField ( fill = FieldFill.INSERT )
  private LocalDateTime createTime;

  @TableLogic
  private Integer isDeleted;

}
