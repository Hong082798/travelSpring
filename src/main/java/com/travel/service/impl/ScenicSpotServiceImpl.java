package com.travel.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travel.dto.ScenicSpotQueryDTO;
import com.travel.entity.ScenicSpot;
import com.travel.mapper.ScenicSpotMapper;
import com.travel.service.ScenicSpotService;
import com.travel.vo.ScenicSpotVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ScenicSpotServiceImpl
    extends ServiceImpl < ScenicSpotMapper, ScenicSpot >
    implements ScenicSpotService {

  @Override
  public IPage < ScenicSpotVO > pageQuery( ScenicSpotQueryDTO queryDTO ) {
    // 1. 构建分页对象
    Page < ScenicSpotVO > page = new Page <>( queryDTO.getPageNum(), queryDTO.getPageSize() );

    // 2. 构建查询条件并执行分页查询
    return this.baseMapper.pageQueryVO( page, queryDTO.getCategoryId(), queryDTO.getKeyword() );
  }

  @Override
  public ScenicSpotVO getDetailById( Long id ) {
    return this.baseMapper.getDetailById( id );
  }
}