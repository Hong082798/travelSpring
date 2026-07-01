package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends BaseMapper < Role > {

  /**
   * 查询某用户拥有的所有角色编码（["admin"，"user"]）
   *
   * @return 角色code列表
   * @params userId 用户id
   *
   */
  List < String > getRoleCodeByUserId( @Param ( "userId" ) Long userId );

}
