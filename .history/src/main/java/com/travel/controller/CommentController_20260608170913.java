package com.travel.controller;

import com.travel.common.Result;
import com.travel.dto.CommentDTO;
import com.travel.service.CommentService;
import com.travel.vo.CommentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag ( name = "评论管理", description = "景点评论相关接口" )
@RestController
@RequestMapping ( "/api/comments" )
public class CommentController {

  @Autowired
  private CommentService commentService;

  @Operation ( summary = "添加评论", description = "用户对景点进行评论" )
  @PostMapping ( "/{scenicId}" )
  public Result < Void > add( @PathVariable Long scenicId, @Valid @RequestBody CommentDTO commentDTO ) {
    commentService.addComment( scenicId, commentDTO );
    return Result.success();
  }

  @Operation ( summary = "查询景点评论列表", description = "对景点发表评论并打分（需要登录）" )
  @GetMapping ( "/scenid/{scenicId}" )
  public Result < List < CommentVO > > listByScenic( @PathVariable Long scenicId ) {
    List < CommentVO > comments = commentService.getCommentByScenicId( scenicId );
    return Result.success( comments );
  }

}
