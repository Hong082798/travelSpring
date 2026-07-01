package com.travel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.travel.common.Result;
import com.travel.dto.CommentDTO;
import com.travel.service.CommentService;
import com.travel.vo.CommentVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag ( name = "评论管理", description = "景点评论相关接口" )
@RestController
@RequestMapping ( "/api/comments" )
public class CommentController {

  private final CommentService commentService;

  CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

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

  @Operation ( summary = "评论分页列表" )
  @GetMapping ( "/page" )
  public Result < IPage < CommentVO > > getCommentPage(
      @RequestParam ( defaultValue = "1" ) Integer PageNum,
      @RequestParam ( defaultValue = "10" ) Integer pageSize,
      @RequestParam ( required = false ) String keyword
  ) {
    return Result.success( commentService.getCommentPage( PageNum, pageSize, keyword ) );
  }

  @Operation ( summary = "删除评论" )
  @DeleteMapping ( "/{id}" )
  public Result < Void > deleteComment( @PathVariable Long id ) {
    commentService.deleteComment( id );
    return Result.success();
  }

}
