package com.travel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema ( description = "景点展示对象" )
public class ScenicSpotVO {
  @Schema ( description = "景点ID" )
  private Long id;

  @Schema ( description = "所属分类ID" )
  private Long categoryId;

  @Schema ( description = "分类名称" )
  private String categoryName;

  @Schema ( description = "景点名称" )
  private String name;

  @Schema ( description = "景点介绍" )
  private String description;

  @Schema ( description = "封面图URL" )
  private String coverImage;

  @Schema ( description = "详细地址" )
  private String address;

  @Schema ( description = "经度" )
  private String longitude;

  @Schema ( description = "维度" )
  private String latitude;

  @Schema ( description = "门票价格" )
  private String ticketPrice;

  @Schema ( description = "评分" )
  private String score;

  @Schema ( description = "状态（0-下架，1-上架）" )
  private Integer status;

  @Schema ( description = "创建时间" )
  private LocalDateTime createTime;
}
