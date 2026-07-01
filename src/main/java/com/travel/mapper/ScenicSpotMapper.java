package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.entity.ScenicSpot;
import com.travel.vo.CategoryStatsVO;
import com.travel.vo.ScenicSpotVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ScenicSpotMapper extends BaseMapper < ScenicSpot > {

  @Select ( "SELECT s.*, c.name AS category_name " +
      "FROM scenic_spot s " +
      "LEFT JOIN scenic_category c ON s.category_id = c.id " +
      "WHERE s.id = #{id} AND s.is_deleted = 0" )
  ScenicSpotVO getDetailById( @Param ( "id" ) Long id );

  IPage < ScenicSpotVO > pageQueryVO( Page < ScenicSpotVO > page, @Param ( "categoryId" ) Long categoryId, @Param (
      "keyword" ) String keyword );

  List < CategoryStatsVO > countByCategory();
}
