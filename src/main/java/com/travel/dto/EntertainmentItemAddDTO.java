package com.travel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema ( description = "新增玩乐项目DTO，只包含前端可填写的业务字段" )
public class EntertainmentItemAddDTO {

  @NotNull ( message = "所属分类不能为空" )
  @Schema ( description = "所属分类ID" )
  private Long categoryId;

  @NotBlank ( message = "名称不能为空" )
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

  @Schema ( description = "人均消费，0表示暂无价格" )
  private BigDecimal avgPrice;

  @Schema ( description = "营业时间，如：周一至周日 10:00-22:00" )
  private String openTime;

  @Schema ( description = "联系电话" )
  private String phone;

  @Schema ( description = "标签，多个用英文逗号分隔" )
  private String tags;

  @Schema ( description = "排序权重，越小越靠前" )
  private Integer sort;

  @Min ( value = 0, message = "状态只能是0下架或1上架" )
  @Max ( value = 1, message = "状态只能是0下架或1上架" )
  @Schema ( description = "状态：0下架 1上架" )
  private Integer status;

}
