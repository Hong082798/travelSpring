package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.entity.Favorite;
import com.travel.vo.EntertainmentItemVO;
import com.travel.vo.ScenicSpotVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface FavoriteMapper extends BaseMapper < Favorite > {

  @Update ( "UPDATE favorite SET is_deleted = 0 WHERE user_id = #{userId} AND target_type = #{targetType} AND target_id = #{targetId} AND is_deleted != 0" )
  int restoreFavorite(
      @Param ( "userId" ) Long userId,
      @Param ( "targetType" ) String targetType,
      @Param ( "targetId" ) Long targetId
  );

  @Select (
      "SELECT s.*, c.name AS category_name " +
          "FROM favorite f " +
          "LEFT JOIN scenic_spot s ON f.target_id = s.id " +
          "LEFT JOIN scenic_category c ON s.category_id = c.id " +
          "WHERE f.user_id = #{userId} AND f.target_type = 'SCENIC' AND f.is_deleted = 0 AND s.is_deleted = 0 " +
          "ORDER BY f.create_time DESC"
  )
  List < ScenicSpotVO > getMyFavoriteScenics( @Param ( "userId" ) Long userId );

  // 玩乐收藏联表：注意真实表是 entertainment_item，列 price / business_hours 需别名成 avg_price / open_time，
  // 才能被下划线转驼峰自动映射到 EntertainmentItemVO 的 avgPrice / openTime。
  @Select (
      "SELECT e.id, e.category_id, c.name AS category_name, e.name, e.description, " +
          "e.cover_image, e.address, e.longitude, e.latitude, " +
          "e.price AS avg_price, e.business_hours AS open_time, " +
          "e.phone, e.tags, e.score, e.sort, e.status, e.create_time " +
          "FROM favorite f " +
          "LEFT JOIN entertainment_item e ON f.target_id = e.id " +
          "LEFT JOIN entertainment_category c ON e.category_id = c.id " +
          "WHERE f.user_id = #{userId} AND f.target_type = 'ENTERTAINMENT' AND f.is_deleted = 0 AND e.is_deleted = 0 " +
          "ORDER BY f.create_time DESC"
  )
  List < EntertainmentItemVO > getMyFavoriteEntertainments( @Param ( "userId" ) Long userId );

}
