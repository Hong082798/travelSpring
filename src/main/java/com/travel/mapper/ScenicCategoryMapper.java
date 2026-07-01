package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.entity.ScenicCategory;
import com.travel.vo.CategoryStatsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ScenicCategoryMapper extends BaseMapper < ScenicCategory > {


  @Select ( "SELECT c.name AS category_name, COUNT(s.id) AS count " +
      "FROM scenic_spot s " +
      "LEFT JOIN scenic_category c ON s.category_id = c.id " +
      "WHERE s.is_deleted = 0 " +          // ⚠️ 铁律点1：景点表过滤被删
      "AND c.is_deleted = 0 " +            // ⚠️ 铁律点2：分类表也要各自过滤
      "GROUP BY c.id, c.name" )
    // 按分类分组，每组算一个 COUNT
  List < CategoryStatsVO > countByCategory();
  
}
