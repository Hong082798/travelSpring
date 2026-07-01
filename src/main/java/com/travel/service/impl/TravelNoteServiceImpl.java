package com.travel.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travel.dto.TravelNoteDTO;
import com.travel.dto.TravelNoteQueryDTO;
import com.travel.entity.TravelNote;
import com.travel.exception.BusinessException;
import com.travel.mapper.TravelNoteMapper;
import com.travel.service.TravelNoteService;
import com.travel.vo.TravelNoteVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TravelNoteServiceImpl extends ServiceImpl < TravelNoteMapper, TravelNote > implements TravelNoteService {

  @Autowired
  private TravelNoteMapper travelNoteMapper;

  // 添加游记
  @Override
  public void publish( TravelNoteDTO dto ) {

    // 从Token中取出当前登录的用户ID
    Long userId = StpUtil.getLoginIdAsLong();

    // 构建实体
    TravelNote note = new TravelNote();
    BeanUtils.copyProperties( dto, note );
    note.setUserId( userId );

    // 保存
    save( note );

  }

  // 编辑游记
  @Override
  public void update( TravelNoteDTO dto ) {

    // 校验id必须传
    if ( dto.getId() == null ) {
      throw new BusinessException( 400, "游记ID不能为空" );
    }

    // 查出原记录，确定存在
    TravelNote note = getById( dto.getId() );
    if ( note == null ) {
      throw new BusinessException( 404, "游记不存在" );
    }

    // 鉴权，只有作者才能编辑
    Long userId = StpUtil.getLoginIdAsLong();
    if ( !note.getUserId().equals( userId ) ) {
      throw new BusinessException( 403, "无权编辑他人游记" );
    }

    // 把DTO的字段覆盖进实体类，在更新
    BeanUtils.copyProperties( dto, note );
    updateById( note );


  }

  @Override
  public void delete( Long id ) {

    // 查出原记录
    TravelNote note = getById( id );
    if ( note == null ) {
      throw new BusinessException( 404, "游记不存在" );
    }

    // 鉴权，只有作者才能删除自己的游记
    Long userId = StpUtil.getLoginIdAsLong();
    if ( !note.getUserId().equals( userId ) ) {
      throw new BusinessException( 403, "无权删除他人游记" );
    }

    // 逻辑删除
    removeById( id );

  }

  @Override
  public Page < TravelNoteVO > pageQuery( TravelNoteQueryDTO dto, Long userId ) {
    Page < TravelNoteVO > page = new Page <>( dto.getPageNum(), dto.getPageSize() );
    //    Long userId = StpUtil.isLogin() ? StpUtil.getLoginIdAsLong() : null;
    return travelNoteMapper.pageQueryVO( page, dto, userId );
  }

  @Override
  public TravelNoteVO getDetail( Long id, Long userId ) {
    //    Long userId = StpUtil.isLogin() ? StpUtil.getLoginIdAsLong() : null;
    TravelNoteVO vo = baseMapper.getDetailById( id, userId );
    if ( vo == null ) {
      throw new BusinessException( 404, "游记不存在" );
    }
    return vo;
  }
}
