package com.travel.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travel.dto.*;
import com.travel.entity.User;
import com.travel.exception.BusinessException;
import com.travel.mapper.UserMapper;
import com.travel.service.RoleService;
import com.travel.service.UserService;
import com.travel.vo.UserInfoVO;
import com.travel.vo.UserVO;
import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl < UserMapper, User > implements UserService {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private RoleService roleService;

  // 用户注册
  @Override
  public void register( RegisterDTO registerDTO ) {

    // 校验用户名是否存在
    Long count = this.lambdaQuery().eq( User :: getUsername, registerDTO.getUsername() ).count();
    if ( count > 0 ) {
      throw new BusinessException( "用户名已存在" );
    }

    // 构建User对象
    User user = new User();
    user.setUsername( registerDTO.getUsername() );
    user.setNickname( registerDTO.getNickname() );
    user.setPhone( registerDTO.getPhone() );

    // 密码加密后储存
    String encodedPassword = passwordEncoder.encode( registerDTO.getPassword() );
    user.setPassword( encodedPassword );

    // 保存到数据库中
    this.save( user );

  }

  // 用户登录
  @Override
  public String login( LoginDTO loginDTO ) {

    // 根据用户查询名称
    User user = this.lambdaQuery().eq( User :: getUsername, loginDTO.getUsername() ).one();

    // 判断用户在不在
    if ( user == null ) {
      throw new BusinessException( "用户名不存在" );
    }

    // 校验密码是否匹配
    boolean matches = passwordEncoder.matches( loginDTO.getPassword(), user.getPassword() );
    if ( !matches ) {
      throw new BusinessException( "密码错误" );
    }

    // 校验账号状态
    if ( user.getStatus() != null && user.getStatus() == 1 ) {
      throw new BusinessException( "账号已被禁用" );
    }

    // 登录成功，升成token
    StpUtil.login( user.getId() );

    // 返回Token
    return StpUtil.getTokenValue();
  }

  // 修改用户资料
  @Override
  public void updateProfile( UserUpdateDTO userUpdateDTO ) {

    // 获取当前登录用户ID
    Long userId = StpUtil.getLoginIdAsLong();

    // 创建User对象，拷贝DTO的字段
    User user = new User();
    BeanUtils.copyProperties( userUpdateDTO, user );

    // 设置更新的用户ID
    user.setId( userId );

    // 更新数据库
    this.updateById( user );

  }

  // 用户修改密码
  @Override
  public void updatePassword( UpdatePasswordDTO updatePasswordDTO ) {

    // 取当前登录的用户
    Long userId = StpUtil.getLoginIdAsLong();
    User user = this.getById( userId );

    // 校验旧密码
    boolean matches = passwordEncoder.matches( updatePasswordDTO.getOldPassword(), user.getPassword() );
    if ( !matches ) {
      throw new BusinessException( "旧密码错误" );
    }

    // 加密新密码并更新
    User update = new User();
    update.setId( userId );
    update.setPassword( passwordEncoder.encode( updatePasswordDTO.getNewPassword() ) );
    this.updateById( update );

  }

  // 分页查询用户列表
  @Override
  public Page < UserVO > pageQuery( UserQueryDTO dto ) {

    Page < UserVO > page = new Page <>( dto.getPageNum(), dto.getPageSize() );
    baseMapper.pageQueryVO( page, dto );

    return page;
  }

  // 修改用户状态,安全校验，不允许自己操作
  @Override
  public void updateStatus( Long id, Integer status ) {

    // 获取当前登录用户id
    Long currentUserId = StpUtil.getLoginIdAsLong();

    // 核心防护——目标 id 和当前登录 id 相同，就是在操作自己，拒绝。
    // 用 equals 而不是 ==：Long 是包装类型，== 比的是对象引用，
    // 数值超过 -128~127 缓存范围时 == 会判错，equals 比的才是值。
    if ( currentUserId.equals( id ) ) {
      // 抛出异常错误
      throw new BusinessException( 400, "不能修改自己的状态" );
    }

    // 校验状态值合法（只能是 0 或 1），防止前端传乱七八糟的值。
    if ( status == null || ( status != 0 && status != 1 ) ) {

      throw new BusinessException( 400, "状态值不合规" );

    }

    // 校验状态值的合法性
    if ( status == null || ( status != 0 && status != 1 ) ) {
      throw new BusinessException( 400, "状态值不合法" );
    }

    // 验证用户存在
    User user = getById( id );
    if ( user == null ) {
      throw new BusinessException( 404, "用户不存在" );
    }

    // 更新状态
    User update = new User();
    update.setId( id );
    update.setStatus( status );
    updateById( update );

  }

  // 删除用户（逻辑删除）
  @Override
  public void deleteUser( Long id ) {

    // 获取当前登录用户信息
    Long currentUserId = StpUtil.getLoginIdAsLong();

    // 同样防护用户不能删除自己
    if ( currentUserId.equals( id ) ) {
      throw new BusinessException( 400, "不能删除自己" );
    }

    // 验证用户存在
    User user = getById( id );
    if ( user == null ) {
      throw new BusinessException( 404, "用户不存在" );
    }

    // 逻辑删除
    removeById( id );

  }

  @Override
  public UserInfoVO getUserInfo() {
    // 拿当前用户ID
    Long userId = StpUtil.getLoginIdAsLong();

    // 查用户实体
    User user = getById( userId );
    if ( user == null ) {
      throw new BusinessException( 404, "用户不存在！" );
    }

    // 组装VO
    UserInfoVO vo = new UserInfoVO();
    BeanUtils.copyProperties( user, vo );

    vo.setRoles( roleService.getRoleCodesByUserId( userId ) );
    return vo;

  }

}
