package com.travel.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.travel.dto.*;
import com.travel.entity.User;
import com.travel.vo.UserInfoVO;
import com.travel.vo.UserVO;

public interface UserService extends IService < User > {

  // 用户注册
  void register( RegisterDTO registerDTO );

  // 用户登录
  String login( LoginDTO loginDTO );

  // 修改用户资料
  void updateProfile( UserUpdateDTO userUpdateDTO );

  // 修改密码
  void updatePassword( UpdatePasswordDTO updatePasswordDTO );

  // 分页查询用户列表
  Page < UserVO > pageQuery( UserQueryDTO dto );

  // 修改用户状态
  void updateStatus( Long id, Integer status );

  // 删除用户(逻辑删除)
  void deleteUser( Long id );

  // 获取当前登录用户信息（含角色）
  UserInfoVO getUserInfo();

}
