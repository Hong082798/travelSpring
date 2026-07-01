package com.travel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.common.Result;
import com.travel.dto.LoginDTO;
import com.travel.dto.RegisterDTO;
import com.travel.dto.UpdatePasswordDTO;
import com.travel.dto.UserQueryDTO;
import com.travel.dto.UserUpdateDTO;
import com.travel.entity.User;
import com.travel.service.UserService;
import com.travel.vo.UserInfoVO;
import com.travel.vo.UserVO;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag ( name = "用户接口", description = "用户相关的API接口" )
@RestController
@RequestMapping ( "/api/users" )
public class UserController {

  @Autowired
  private UserService userService;

  // @Operation ( summary = "获取用户列表", description = "返回所有用户的列表" )
  // @GetMapping ( "/list" )
  // public Result < List < User > > list() {

  //   List < User > users = userService.list();

  //   return Result.success( users );
  // }

  @Operation ( summary = "用户注册", description = "注册新用户" )
  @PostMapping ( "/register" )
  public Result < Void > register( @Valid @RequestBody RegisterDTO registerDTO ) {
    userService.register( registerDTO );
    return Result.success();
  }

  @Operation ( summary = "用户登录", description = "用户登录接口，返回JWT令牌" )
  @PostMapping ( "/login" )
  public Result < String > login( @Valid @RequestBody LoginDTO loginDTO ) {
    String token = userService.login( loginDTO );
    return Result.success( token );
  }

  @Operation ( summary = "获取当前登录用户", description = "返回当前登录用户的信息" )
  @GetMapping ( "/info" )
  public Result < UserInfoVO > info() {
    return Result.success( userService.getUserInfo() );
  }

  @Operation ( summary = "修改个人资料", description = "修改当前登录用户的资料（需要登录）" )
  @PutMapping ( "/profile" )
  public Result < Void > updateProfile( @Valid @RequestBody UserUpdateDTO updateDTO ) {
    userService.updateProfile( updateDTO );
    return Result.success();
  }

  @Operation ( summary = "修改密码", description = "需要登录" )
  @PutMapping ( "/password" )
  public Result < Void > updatePassword( @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO ) {
    userService.updatePassword( updatePasswordDTO );
    return Result.success();
  }

  @Operation ( summary = "退出登录", description = "注销当前登录用户" )
  @PostMapping ( "/logout" )
  public Result < Void > logout() {
    StpUtil.logout();
    return Result.success();
  }

  @Operation ( summary = "用户列表分页查询", description = "管理后台用，支持搜索和状态筛选" )
  @SaCheckRole ( "admin" ) // 只有拥有admin角色才能访问，否则Sa-Token会抛出异常-403
  @PostMapping ( "/page" )
  public Result < Page < UserVO > > pageQuery( @RequestBody UserQueryDTO dto ) {
    StpUtil.checkLogin();
    return Result.success( userService.pageQuery( dto ) );
  }

  @Operation ( summary = "修改用户状态", description = "启用或者禁用用户" )
  @SaCheckRole ( "admin" )
  @PutMapping ( "/{id}/status" )
  public Result < Void > updateStatus( @PathVariable Long id, @PathVariable Integer status ) {
    StpUtil.checkLogin();
    userService.updateStatus( id, status );
    return Result.success();
  }

  @Operation ( summary = "删除用户", description = "逻辑删除用户" )
  @SaCheckRole ( "admin" )
  @DeleteMapping ( "/{id}" )
  public Result < Void > deleteUser( @PathVariable Long id ) {
    StpUtil.checkLogin();
    userService.deleteUser( id );
    return Result.success();
  }

}
