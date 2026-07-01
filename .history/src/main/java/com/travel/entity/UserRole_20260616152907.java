package com.travel.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

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
