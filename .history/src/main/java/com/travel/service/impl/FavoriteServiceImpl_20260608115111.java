package com.travel.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travel.entity.Favorite;
import com.travel.mapper.FavoriteMapper;
import com.travel.service.FavoriteService;
import com.travel.vo.ScenicSpotVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl extends ServiceImpl < FavoriteMapper, Favorite > implements FavoriteService {
  @Override
  public void addFavorite( Long scenicId ) {

    // 收藏必须绑定当前登录用户，不能由前端传 userId，避免越权替别人写收藏数据。
    Long userId = StpUtil.getLoginIdAsLong();

    // 先查重再新增，目的是把“同一用户同一景点只能收藏一次”这个业务约束收敛在服务层，
    // 这样可以提前返回明确提示，也避免重复数据影响收藏列表和收藏状态判断。
    Long count = this.lambdaQuery().eq( Favorite :: getUserId, userId ).eq( Favorite :: getScenicId, scenicId ).count();

    if ( count > 0 ) {
      throw new RuntimeException( "已经收藏过了" );
    }

    // 这里只保存 userId 和 scenicId，其他字段交给数据库默认值或 MyBatis-Plus 自动填充处理，
    // 保持写入字段最小化，避免把与当前业务无关的数据耦合进来。
    Favorite favorite = new Favorite();
    favorite.setUserId( userId );
    favorite.setScenicId( scenicId );
    this.save( favorite );

  }

  @Override
  public void cancelFavorite( Long scenicId ) {

    Long userId = StpUtil.getLoginIdAsLong();

    // 按 userId + scenicId 定位记录，确保只会取消“当前用户自己的这条收藏”。
    // 这里调用 remove，是因为项目已经为 favorite 表设计了 isDeleted 字段，
    // 在启用逻辑删除的前提下，remove 会转成更新删除标记，而不是物理删库，
    // 这样既保留历史数据，也能让后续查询天然过滤掉已取消的收藏。
    this.lambdaUpdate().eq( Favorite :: getUserId, userId ).eq( Favorite :: getScenicId, scenicId ).remove();

  }

  @Override
  public List < ScenicSpotVO > getMyFavorites() {

    Long userId = StpUtil.getLoginIdAsLong();

    // 这里直接走 Mapper 的联表查询，而不是先查 favorite 再逐条查景点，
    // 目的是一次性拿到列表页真正需要的景点和分类展示数据，避免 N + 1 查询。
    return this.baseMapper.getMyFavorites( userId );
  }

  @Override
  public boolean isFavorites( Long scenicId ) {

    Long userId = StpUtil.getLoginIdAsLong();

    // 用 count 判断是否存在，语义直接，对当前这种按用户和景点双条件过滤的场景足够清晰。
    // 同时复用和新增收藏一致的条件，保证“列表状态”和“实际收藏记录”判断口径一致。
    Long count = this.lambdaQuery().eq( Favorite :: getUserId, userId ).eq( Favorite :: getScenicId, scenicId ).count();

    return count > 0;
  }
}
