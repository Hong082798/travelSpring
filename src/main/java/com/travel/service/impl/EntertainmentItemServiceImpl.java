package com.travel.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travel.dto.EntertainmentItemAddDTO;
import com.travel.dto.EntertainmentItemQueryDTO;
import com.travel.dto.EntertainmentItemUpdateDTO;
import com.travel.entity.EntertainmentItem;
import com.travel.exception.BusinessException;
import com.travel.mapper.EntertainmentItemMapper;
import com.travel.service.EntertainmentItemService;
import com.travel.vo.EntertainmentItemVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class EntertainmentItemServiceImpl extends ServiceImpl < EntertainmentItemMapper, EntertainmentItem >
    implements EntertainmentItemService {
  @Override
  public IPage < EntertainmentItemVO > pageQuery( EntertainmentItemQueryDTO queryDTO ) {

    Page < EntertainmentItemVO > page = new Page <>(
        queryDTO.getPageNum(),
        queryDTO.getPageSize()
    );
    return this.baseMapper.pageQueryVO(
        page,
        queryDTO.getCategoryId(),
        queryDTO.getKeyword(),
        queryDTO.getStatus()
    );
  }

  @Override
  public EntertainmentItemVO getDetailById( Long id ) {

    return this.baseMapper.getDetailById( id );

  }

  @Override
  public void addItem( EntertainmentItemAddDTO dto ) {
    // 只把 DTO 里的业务字段拷进实体，id/score/createTime/isDeleted 等不在 DTO 里，
    // 自然不会被前端污染，id 留空交给数据库自增。
    EntertainmentItem item = new EntertainmentItem();
    BeanUtils.copyProperties( dto, item );
    this.save( item );
  }

  @Override
  public EntertainmentItemVO updateItem( EntertainmentItemUpdateDTO dto ) {
    // 先确认记录存在，避免对不存在的 id 执行无意义的更新。
    EntertainmentItem exist = this.getById( dto.getId() );
    if ( exist == null ) {
      throw new BusinessException( 404, "玩乐项目不存在" );
    }
    validateStatus( dto.getStatus() );

    // 同样只覆盖 DTO 里的字段，createTime/isDeleted/score 等保持原值不被前端改动。
    EntertainmentItem item = new EntertainmentItem();
    BeanUtils.copyProperties( dto, item );
    this.updateById( item );
    return this.baseMapper.getDetailById( dto.getId() );
  }

  @Override
  public EntertainmentItemVO updateStatus( Long id, Integer status ) {
    if ( id == null ) {
      throw new BusinessException( 400, "玩乐项目ID不能为空" );
    }
    validateStatus( status );

    EntertainmentItem exist = this.getById( id );
    if ( exist == null ) {
      throw new BusinessException( 404, "玩乐项目不存在" );
    }

    EntertainmentItem item = new EntertainmentItem();
    item.setId( id );
    item.setStatus( status );
    this.updateById( item );
    return this.baseMapper.getDetailById( id );
  }

  private void validateStatus( Integer status ) {
    if ( status == null ) {
      return;
    }
    if ( status != 0 && status != 1 ) {
      throw new BusinessException( 400, "状态只能是0下架或1上架" );
    }
  }
}
