package com.travel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.travel.dto.ScenicSpotQueryDTO;
import com.travel.entity.ScenicSpot;
import com.travel.vo.ScenicSpotVO;


public interface ScenicSpotService extends IService < ScenicSpot > {

  // 查询景点列表（分页）
  IPage < ScenicSpotVO > pageQuery( ScenicSpotQueryDTO queryDTO );

  // 查询景点详情
  ScenicSpotVO getDetailById( Long id );

}
