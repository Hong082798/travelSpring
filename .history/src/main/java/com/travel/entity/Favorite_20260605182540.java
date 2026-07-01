package com.travel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName ( "favorite" )
@Schema ( description = "收藏实体类，包含用户收藏的景点信息" )
public class Favorite {

  @TableId ( type = IdType.AUTO )
  @Schema ( description = "收藏ID，主键，自增", example = "1" )
  private Long id;

  @Schema ( description = "用户ID，外键，关联用户表", example = "1" )
  private Long userId;

  @Schema ( description = "景点ID，外键，关联景点表", example = "1" )
  private Long scenicId;

  @Schema ( description = "收藏时间", example = "2024-06-01T12:00:00" )
  private LocalDateTime createTime;

  @Schema ( description = "逻辑删除标志", example = "0" )
  private Integer isDeleted;

}
