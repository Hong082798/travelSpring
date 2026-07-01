package com.travel.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travel.entity.Role;
import com.travel.mapper.RoleMapper;
import com.travel.service.RoleService;

@Service
public class RoleServiceImpl extends ServiceImpl < RoleMapper, Role > implements RoleService {
  @Override
  public List < String > getRoleCodesByUserId( Long userId ) {
    // 直接转调用Mapper，这里很薄，但保留Service层是为了：
    // 统一架构，将来可能要加缓存
    return baseMapper.getRoleCodeByUserId( userId );
  }
}
