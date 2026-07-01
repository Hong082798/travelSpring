package com.travel.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.common.Result;
import com.travel.dto.TravelNoteDTO;
import com.travel.dto.TravelNoteQueryDTO;
import com.travel.service.NoteLikeService;
import com.travel.service.TravelNoteService;
import com.travel.vo.TravelNoteVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag ( name = "游记模块" )
@RestController
@RequestMapping ( "/api/notes" )
public class TravelNoteController {

  @Autowired
  private TravelNoteService travelNoteService;

  @Autowired
  private NoteLikeService noteLikeService;

  @Operation ( summary = "发布游记" )
  @PostMapping ( "/publish" )
  public Result < Void > publish( @Valid @RequestBody TravelNoteDTO dto ) {
    StpUtil.checkLogin();
    travelNoteService.publish( dto );
    return Result.success();
  }

  @Operation ( summary = "编辑游记" )
  @PutMapping ( "/update" )
  public Result < Void > update( @Valid @RequestBody TravelNoteDTO dto ) {
    StpUtil.checkLogin();
    travelNoteService.update( dto );
    return Result.success();
  }

  @Operation ( summary = "删除游记" )
  @DeleteMapping ( "/{id}" )
  public Result < Void > delete( @PathVariable Long id ) {
    StpUtil.checkLogin();
    travelNoteService.delete( id );
    return Result.success();
  }

  @Operation ( summary = "游记列表查询" )
  @PostMapping ( "/page" )
  public Result < Page < TravelNoteVO > > pageQuery( @RequestBody TravelNoteQueryDTO dto ) {
    Long userId = StpUtil.isLogin() ? StpUtil.getLoginIdAsLong() : null;
    return Result.success( travelNoteService.pageQuery( dto, userId ) );
  }

  @Operation ( summary = "点赞游记" )
  @PostMapping ( "/{noteId}/like" )
  public Result < Void > like( @PathVariable Long noteId ) {
    StpUtil.checkLogin();
    noteLikeService.like( noteId );
    return Result.success();
  }

  @Operation ( summary = "取消点赞游记" )
  @DeleteMapping ( "/{noteId}/like" )
  public Result < Void > cancelLike( @PathVariable Long noteId ) {
    StpUtil.checkLogin();
    noteLikeService.cancelLike( noteId );
    return Result.success();
  }

  @Operation ( summary = "游记详情" )
  @GetMapping ( "/{id}" )
  public Result < TravelNoteVO > getDetail( @PathVariable Long id ) {
    Long userId = StpUtil.isLogin() ? StpUtil.getLoginIdAsLong() : null;
    return Result.success( travelNoteService.getDetail( id, userId ) );
  }
}
