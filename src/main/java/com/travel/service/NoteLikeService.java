package com.travel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.travel.entity.NoteLike;

public interface NoteLikeService extends IService < NoteLike > {

  // 点赞游记
  void like( Long noteId );

  // 取消点赞游记
  void cancelLike( Long noteId );

  // 私有方法isLiked
  boolean isLiked( Long noteId, Long userId );
}
