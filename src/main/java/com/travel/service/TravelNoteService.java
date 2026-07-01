package com.travel.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.travel.dto.TravelNoteDTO;
import com.travel.dto.TravelNoteQueryDTO;
import com.travel.entity.TravelNote;
import com.travel.vo.TravelNoteVO;

public interface TravelNoteService extends IService < TravelNote > {

  // 发布游记
  void publish( TravelNoteDTO dto );

  // 编辑游记
  void update( TravelNoteDTO dto );

  // 删除游记（逻辑删除，只有作者可以删除）
  void delete( Long id );

  // 分页查询
  Page < TravelNoteVO > pageQuery( TravelNoteQueryDTO dto, Long userId );

  // 详情
  TravelNoteVO getDetail( Long id, Long userId );
}
