package com.travel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.travel.dto.EntertainmentItemAddDTO;
import com.travel.dto.EntertainmentItemQueryDTO;
import com.travel.dto.EntertainmentItemUpdateDTO;
import com.travel.entity.EntertainmentItem;
import com.travel.vo.EntertainmentItemVO;

public interface EntertainmentItemService extends IService < EntertainmentItem > {

  IPage < EntertainmentItemVO > pageQuery( EntertainmentItemQueryDTO queryDTO );

  EntertainmentItemVO getDetailById( Long id );

  // 新增玩乐项目（只接受 DTO 里的业务字段，id 由数据库自增）
  void addItem( EntertainmentItemAddDTO dto );

  // 修改玩乐项目（按 DTO 的 id 定位，只覆盖允许修改的字段）
  EntertainmentItemVO updateItem( EntertainmentItemUpdateDTO dto );

  // 修改玩乐项目状态：0下架，1上架
  EntertainmentItemVO updateStatus( Long id, Integer status );

}
