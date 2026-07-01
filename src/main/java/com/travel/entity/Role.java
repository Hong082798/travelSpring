package com.travel.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema ( description = "role" )
public class Role {

  @TableId ( type = IdType.AUTO ) // 主键自增
  private Long id;

  private String code;
  private String name;

  @TableField ( fill = FieldFill.INSERT ) // 插入时自动填充
  private LocalDateTime createTime;

  @TableField ( fill = FieldFill.INSERT_UPDATE )
  private LocalDateTime updateTime;

  @TableLogic // 逻辑删除标记
  private Integer isDeleted;

}
