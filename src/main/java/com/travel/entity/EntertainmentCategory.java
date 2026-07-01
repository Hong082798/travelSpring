package com.travel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName ( "entertainment_category" )
@Schema ( description = "玩乐分类实体类" )
public class EntertainmentCategory {

  @TableId ( type = IdType.AUTO )
  @Schema ( description = "玩乐分类ID" )
  private Long id;

  @Schema ( description = "玩乐分类名称" )
  private String name;

  @Schema ( description = "排序值，越小越靠前" )
  private Integer sort;

  @Schema ( description = "状态：0-禁用，1-启用" )
  private Integer status;

  @Schema ( description = "创建时间" )
  private LocalDateTime createTime;

  @Schema ( description = "更新时间" )
  private LocalDateTime updateTime;

  @Schema ( description = "逻辑删除标志：0-未删除，1-已删除" )
  private Integer isDeleted;
  
}
