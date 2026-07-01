package com.travel.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travel.entity.Favorite;
import com.travel.exception.BusinessException;
import com.travel.mapper.FavoriteMapper;
import com.travel.service.FavoriteService;
import com.travel.vo.EntertainmentItemVO;
import com.travel.vo.ScenicSpotVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl extends ServiceImpl < FavoriteMapper, Favorite > implements FavoriteService {

  private static final String TARGET_TYPE_SCENIC = "SCENIC";

  private static final String TARGET_TYPE_ENTERTAINMENT = "ENTERTAINMENT";

  @Override
  public void addFavorite( String targetType, Long targetId ) {

    // targetType 现在来自客户端，先归一化并校验，避免脏类型落库后污染后续按类型过滤的查询。
    String type = normalizeTargetType( targetType );
    Long userId = StpUtil.getLoginIdAsLong();

    // 检查是否已有 is_deleted=0 的活跃收藏，避免重复收藏
    boolean alreadyActive = this.lambdaQuery()
        .eq( Favorite :: getUserId, userId )
        .eq( Favorite :: getTargetType, type )
        .eq( Favorite :: getTargetId, targetId )
        .count() > 0;
    if ( alreadyActive ) {
      throw new BusinessException( 400, "已经收藏过了" );
    }

    // 用原生 SQL 复活软删除的记录，绕过 MP 全局软删除拦截器（Wrapper 方式会被自动追加 AND is_deleted=0，导致永远匹配不到）
    boolean restored = baseMapper.restoreFavorite( userId, type, targetId ) > 0;

    if ( !restored ) {
      Favorite favorite = new Favorite();
      favorite.setUserId( userId );
      favorite.setTargetType( type );
      favorite.setTargetId( targetId );
      favorite.setIsDeleted( 0 );
      this.save( favorite );
    }

  }

  @Override
  public void cancelFavorite( String targetType, Long targetId ) {

    String type = normalizeTargetType( targetType );
    Long userId = StpUtil.getLoginIdAsLong();

    // 按 userId + targetType + targetId 定位记录，确保只会取消“当前用户自己的这条收藏”。
    // 这里调用 remove，是因为项目已经为 favorite 表设计了 isDeleted 字段，
    // 在启用逻辑删除的前提下，remove 会转成更新删除标记，而不是物理删库，
    // 这样既保留历史数据，也能让后续查询天然过滤掉已取消的收藏。
    this.lambdaUpdate()
        .eq( Favorite :: getUserId, userId )
        .eq( Favorite :: getTargetType, type )
        .eq( Favorite :: getTargetId, targetId )
        .remove();

  }

  @Override
  public boolean isFavorites( String targetType, Long targetId ) {

    String type = normalizeTargetType( targetType );
    Long userId = StpUtil.getLoginIdAsLong();

    // 用 count 判断是否存在，语义直接，对当前这种按用户和目标双条件过滤的场景足够清晰。
    // 同时复用和新增收藏一致的条件，保证“列表状态”和“实际收藏记录”判断口径一致。
    Long count = this.lambdaQuery()
        .eq( Favorite :: getUserId, userId )
        .eq( Favorite :: getTargetType, type )
        .eq( Favorite :: getTargetId, targetId )
        .count();

    return count > 0;
  }

  @Override
  public List < ScenicSpotVO > getMyFavoriteScenics() {

    Long userId = StpUtil.getLoginIdAsLong();

    // 直接走 Mapper 联表查询，一次性拿到收藏的景点 + 分类展示数据，避免先查 favorite 再逐条查景点的 N+1。
    return this.baseMapper.getMyFavoriteScenics( userId );
  }

  @Override
  public List < EntertainmentItemVO > getMyFavoriteEntertainments() {

    Long userId = StpUtil.getLoginIdAsLong();

    // 玩乐 Tab 与景点 Tab 完全对称：同样一次联表拿全玩乐 VO，只是目标表换成 entertainment_item。
    return this.baseMapper.getMyFavoriteEntertainments( userId );
  }

  /**
   * 归一化并校验收藏目标类型。
   * 兼容大小写写法（如 "scenic"），并拦截未知类型，避免脏 target_type 落库。
   */
  private String normalizeTargetType( String targetType ) {
    if ( targetType == null || targetType.trim().isEmpty() ) {
      throw new BusinessException( 400, "收藏类型不能为空" );
    }
    String type = targetType.trim().toUpperCase();
    if ( !TARGET_TYPE_SCENIC.equals( type ) && !TARGET_TYPE_ENTERTAINMENT.equals( type ) ) {
      throw new BusinessException( 400, "不支持的收藏类型: " + targetType );
    }
    return type;
  }
}