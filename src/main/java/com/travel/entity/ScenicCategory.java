package com.travel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName ( "scenic_category" )
@Schema ( description = "景点分类实体类，包含景点分类的基本信息" )
public class ScenicCategory {

  @TableId ( type = IdType.AUTO )
  @Schema ( description = "景点分类ID，主键，自增", example = "1" )
  private Long id;

  @Schema ( description = "景点分类名称", example = "自然风光" )
  private String name;

  @Schema ( description = "排序" )
  private Integer sort;

  @Schema ( description = "创建时间" )
  private String createTime;

  @Schema ( description = "更新时间" )
  private String updateTime;

  @Schema ( description = "逻辑删除" )
  private Integer isDeleted;

}
