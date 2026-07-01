package com.travel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName ( "note_like" )
public class NoteLike {

  @TableId ( type = IdType.AUTO )
  private Long id;
  private Long userId;
  private Long noteId;
  private LocalDateTime createTime;
  private Integer isDeleted;

}
