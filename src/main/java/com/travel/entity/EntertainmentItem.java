package com.travel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName ( "entertainment_item" )
@Schema ( description = "玩乐场所实体类" )
public class EntertainmentItem {

  @TableId ( type = IdType.AUTO )
  @Schema ( description = "主键ID" )
  private Long id;

  @Schema ( description = "所属分类ID" )
  private Long categoryId;

  @Schema ( description = "场所名称" )
  private String name;

  @Schema ( description = "详细描述" )
  private String description;

  @Schema ( description = "封面图URL" )
  private String coverImage;           // 对应数据库 cover_image

  @Schema ( description = "详细地址" )
  private String address;

  @Schema ( description = "经度" )
  private BigDecimal longitude;        // decimal(10,6) 用 BigDecimal，不用 Double

  @Schema ( description = "纬度" )
  private BigDecimal latitude;

  @Schema ( description = "人均消费，0表示暂无价格" )
  @TableField ( "price" )              // 数据库列名是 price，Java/JSON 字段保持 avgPrice
  private BigDecimal avgPrice;

  @Schema ( description = "营业时间，如：周一至周日 10:00-22:00" )
  @TableField ( "business_hours" )     // 数据库列名是 business_hours，Java/JSON 字段保持 openTime
  private String openTime;

  @Schema ( description = "联系电话" )
  private String phone;

  @Schema ( description = "标签，多个用英文逗号分隔" )
  private String tags;                 // 对应数据库 tags

  @Schema ( description = "评分，由评论平均分更新" )
  private BigDecimal score;

  @Schema ( description = "排序权重，越小越靠前" )
  private Integer sort;                // 对应数据库 sort

  @Schema ( description = "状态：0下架 1上架" )
  private Integer status;

  @Schema ( description = "创建时间" )
  private LocalDateTime createTime;

  @Schema ( description = "更新时间" )
  private LocalDateTime updateTime;

  @TableLogic
  @Schema ( description = "软删除：0正常 1已删除" )
  private Integer isDeleted;
}
