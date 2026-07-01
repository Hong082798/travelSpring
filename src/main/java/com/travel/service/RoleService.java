package com.travel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.travel.entity.Role;

import java.util.List;

public interface RoleService extends IService < Role > {

  // 查询用户的角色编码列表
  List < String > getRoleCodesByUserId( Long userId );

}
