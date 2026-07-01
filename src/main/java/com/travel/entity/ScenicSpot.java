package com.travel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName ( "scenic_spot" )
@Schema ( description = "景点实体类，包含景点的基本信息" )
public class ScenicSpot {

  @TableId ( type = IdType.AUTO )
  @Schema ( description = "景点ID，主键，自增", example = "1" )
  private Long id;

  @Schema ( description = "所属分类ID", example = "1" )
  private Long categoryId;

  @Schema ( description = "景点名称" )
  private String name;

  @Schema ( description = "景点介绍" )
  private String description;

  @Schema ( description = "封面图URL" )
  private String coverImage;

  @Schema ( description = "详细地址" )
  private String address;

  @Schema ( description = "经度" )
  private BigDecimal longitude;

  @Schema ( description = "维度" )
  private BigDecimal latitude;

  @Schema ( description = "门票价格" )
  private BigDecimal ticketPrice;

  @Schema ( description = "评分" )
  private BigDecimal score;

  @Schema ( description = "状态（0-下架，1-上架）" )
  private Integer status;

  @Schema ( description = "创建时间" )
  private LocalDateTime createTime;

  @Schema ( description = "更新时间" )
  private LocalDateTime updateTime;

  @Schema ( description = "逻辑删除标志（0-未删除，1-已删除）" )
  private Integer isDeleted;


}
