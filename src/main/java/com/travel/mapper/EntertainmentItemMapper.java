package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.entity.EntertainmentItem;
import com.travel.vo.EntertainmentItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EntertainmentItemMapper extends BaseMapper < EntertainmentItem > {

  IPage < EntertainmentItemVO > pageQueryVO(
      Page < EntertainmentItemVO > page,
      @Param ( "categoryId" ) Long categoryId,
      @Param ( "keyword" ) String keyword,
      @Param ( "status" ) Integer status
  );

  EntertainmentItemVO getDetailById( @Param ( "id" ) Long id );

}
