package com.travel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.travel.entity.Favorite;
import com.travel.vo.EntertainmentItemVO;
import com.travel.vo.ScenicSpotVO;

import java.util.List;

public interface FavoriteService extends IService < Favorite > {

  void addFavorite( String targetType, Long targetId );

  void cancelFavorite( String targetType, Long targetId );

  boolean isFavorites( String targetType, Long targetId );

  // 思考题落点：景点 / 玩乐两个 Tab 完全独立、各自返回完整 VO，且两个 VO 没有公共父接口，
  // 字段差异也大。若把 getMyFavorites(targetType) 的返回类型放宽成 Object / List<?> 让调用方转型，
  // 会丢掉编译期类型安全，Swagger 也无法给出准确的响应结构。因此这里选择「拆成两个独立方法 +
  // 各走 Controller 不同路由」，每个方法返回各自确定的 VO 类型。
  List < ScenicSpotVO > getMyFavoriteScenics();

  List < EntertainmentItemVO > getMyFavoriteEntertainments();

}
