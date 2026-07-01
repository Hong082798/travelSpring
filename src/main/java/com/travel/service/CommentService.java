package com.travel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.travel.dto.CommentDTO;
import com.travel.entity.Comment;
import com.travel.vo.CommentVO;

import java.util.List;

public interface CommentService extends IService < Comment > {

  void addComment( Long scenicId, CommentDTO commentDTO );

  List < CommentVO > getCommentByScenicId( Long scenicId );

  IPage < CommentVO > getCommentPage( Integer pageNum, Integer pageSize, String keyword );

  void deleteComment( Long id );

}
