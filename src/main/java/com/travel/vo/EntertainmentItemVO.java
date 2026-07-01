package com.travel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema ( description = "玩乐场所展示对象" )
public class EntertainmentItemVO {

  @Schema ( description = "场所ID" )
  private Long id;

  @Schema ( description = "所属分类ID" )
  private Long categoryId;

  @Schema ( description = "分类名称，JOIN自entertainment_category" )
  private String categoryName;

  @Schema ( description = "场所名称" )
  private String name;

  @Schema ( description = "详细描述" )
  private String description;

  @Schema ( description = "封面图URL" )
  private String coverImage;

  @Schema ( description = "详细地址" )
  private String address;

  @Schema ( description = "经度" )
  private BigDecimal longitude;

  @Schema ( description = "纬度" )
  private BigDecimal latitude;

  @Schema ( description = "人均消费" )
  private BigDecimal avgPrice;

  @Schema ( description = "营业时间" )
  private String openTime;

  @Schema ( description = "联系电话" )
  private String phone;

  @Schema ( description = "标签" )
  private String tags;

  @Schema ( description = "评分" )
  private BigDecimal score;

  @Schema ( description = "排序权重" )
  private Integer sort;

  @Schema ( description = "状态：0下架 1上架" )
  private Integer status;

  @Schema ( description = "创建时间" )
  private LocalDateTime createTime;
}
