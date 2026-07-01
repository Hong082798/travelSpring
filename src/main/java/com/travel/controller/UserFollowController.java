package com.travel.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.common.Result;
import com.travel.service.UserFollowService;
import com.travel.vo.FollowCountVO;
import com.travel.vo.TravelNoteVO;
import com.travel.vo.UserFollowVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag ( name = "关注模块" )
@RestController
@RequestMapping ( "/api/follows" )
public class UserFollowController {

  @Autowired
  private UserFollowService userFollowService;

  @Operation ( summary = "关注用户" )
  @PostMapping ( "/{followingId}" )
  public Result < Void > follow( @PathVariable Long followingId ) {
    StpUtil.checkLogin();
    userFollowService.follow( followingId );
    return Result.success();
  }

  @Operation ( summary = "取消关注" )
  @DeleteMapping ( "/{followingId}" )
  public Result < Void > unfollow( @PathVariable Long followingId ) {
    StpUtil.checkLogin();
    userFollowService.unfollow( followingId );
    return Result.success();
  }

  @Operation ( summary = "我的关注列表" )
  @GetMapping ( "/following" )
  public Result < List < UserFollowVO > > getFollowingList() {
    StpUtil.checkLogin();
    Long userId = StpUtil.getLoginIdAsLong();
    return Result.success( userFollowService.getFollowingList( userId ) );
  }

  @Operation ( summary = "我的粉丝列表" )
  @GetMapping ( "followers" )
  public Result < List < UserFollowVO > > getFoloowList() {
    StpUtil.checkLogin();
    Long userId = StpUtil.getLoginIdAsLong();
    return Result.success( userFollowService.getFollowerList( userId ) );
  }

  @Operation ( summary = "查询用户的关注/粉丝数量" )
  @GetMapping ( "/count/{userId}" )
  public Result < FollowCountVO > getCount( @PathVariable Long userId ) {
    FollowCountVO vo = new FollowCountVO();
    vo.setFollowingCount( userFollowService.getFollowingCount( userId ) );
    vo.setFollowCount( userFollowService.getFollowerCount( userId ) );
    return Result.success( vo );
  }

  @Operation ( description = "是否已关注某人" )
  @GetMapping ( "/check/{followingId}" )
  public Result < Boolean > isFollowing( @PathVariable Long followingId ) {
    StpUtil.checkLogin();
    Long followerId = StpUtil.getLoginIdAsLong();
    return Result.success( userFollowService.isFollowing( followerId, followingId ) );
  }

  @Operation ( description = "关注的人发的游记（Feed流）" )
  @GetMapping ( "/feed" )
  public Result < Page < TravelNoteVO > > getFollowingFeed(
      @RequestParam ( defaultValue = "1" ) Integer pageNum,
      @RequestParam ( defaultValue = "10" ) Integer pageSize
  ) {
    StpUtil.checkLogin();
    return Result.success( userFollowService.getFollowingFeed( pageNum, pageSize ) );
  }

}
