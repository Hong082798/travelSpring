package com.travel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName ( "travel_note" )
@Schema ( description = "游记" )
public class TravelNote {

  @TableId ( type = IdType.AUTO )
  private Long id;
  private Long userId;
  private String title;
  private String content;
  private String coverImage;
  private Long scenicId;
  private Integer status;
  private Integer likeCount;
  private Integer viewCount;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;
  private Integer isDeleted;

}
