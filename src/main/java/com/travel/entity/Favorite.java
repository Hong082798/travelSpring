package com.travel.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName ( "favorite" )
@Schema ( description = "收藏" )
public class Favorite {

  @TableId ( type = IdType.AUTO )
  private Long id;

  private Long userId;

  private String targetType;

  private Long targetId;

  private LocalDateTime createTime;

  @TableLogic
  private Integer isDeleted;

}
