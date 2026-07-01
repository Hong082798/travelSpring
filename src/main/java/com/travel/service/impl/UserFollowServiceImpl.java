package com.travel.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travel.entity.User;
import com.travel.entity.UserFollow;
import com.travel.exception.BusinessException;
import com.travel.mapper.UserFollowMapper;
import com.travel.mapper.UserMapper;
import com.travel.service.UserFollowService;
import com.travel.vo.TravelNoteVO;
import com.travel.vo.UserFollowVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFollowServiceImpl extends ServiceImpl < UserFollowMapper, UserFollow >
    implements UserFollowService {

  @Autowired
  private UserMapper userMapper;

  @Override
  public void follow( Long followingId ) {
    // 关注关系必须以当前登录用户为关注者，避免前端传 followerId 导致越权替别人建立关注关系。
    Long followerId = StpUtil.getLoginIdAsLong();

    // 自己关注自己没有实际业务意义，也会污染关注数、粉丝数和关注动态流的统计结果。
    if ( followerId.equals( followingId ) ) {
      throw new BusinessException( 400, "不能关注自己" );
    }

    // 被关注用户必须真实存在且未被删除，否则不能生成关注关系，避免后续列表联表查询出现无效用户。
    User target = userMapper.selectById( followingId );
    if ( target == null || target.getIsDeleted() == 1 ) {
      throw new BusinessException( 404, "用户不存在" );
    }

    // 同一关注者和被关注者之间只允许存在一条有效关注记录，重复关注直接给出明确业务提示。
    if ( isFollowing( followerId, followingId ) ) {
      throw new BusinessException( 400, "您已关注该用户" );
    }

    // 先尝试复活之前软删除的关注记录，避免唯一约束 uk_follow 冲突。
    // 必须走 Mapper 原生 SQL，因为 Wrapper 方式会被 MP 全局软删除拦截器追加 AND is_deleted=0 而永远匹配不到。
    boolean restored = baseMapper.restoreFollow( followerId, followingId ) > 0;

    if ( !restored ) {
      // 历史上从未关注过，新增一条；显式设置 isDeleted，避免库里存 NULL。
      UserFollow follow = new UserFollow();
      follow.setFollowerId( followerId );
      follow.setFollowingId( followingId );
      follow.setIsDeleted( 0 );
      save( follow );
    }
  }

  @Override
  public void unfollow( Long followingId ) {
    // 取消关注同样只能操作当前登录用户自己的关注关系，不能由前端指定 followerId。
    Long followerId = StpUtil.getLoginIdAsLong();

    // 先判断有效关注关系是否存在，避免对未关注用户执行无意义的删除操作。
    if ( !isFollowing( followerId, followingId ) ) {
      throw new BusinessException( 400, "您尚未关注该用户" );
    }

    // 按 followerId + followingId 精确定位当前用户的关注记录；实体上配置了逻辑删除，
    // remove 会更新删除标记，而不是物理删除数据，方便保留历史关系。
    LambdaUpdateWrapper < UserFollow > wrapper = new LambdaUpdateWrapper <>();
    wrapper.eq( UserFollow :: getFollowerId, followerId )
        .eq( UserFollow :: getFollowingId, followingId );
    remove( wrapper );
  }

  @Override
  public List < UserFollowVO > getFollowingList( Long userId ) {
    // 关注列表需要展示被关注用户的昵称、头像等信息，直接交给 Mapper 做联表查询，避免服务层逐条查用户。
    return baseMapper.getFollowingList( userId );
  }

  @Override
  public List < UserFollowVO > getFollowerList( Long userId ) {
    // 粉丝列表展示的是“谁关注了该用户”，查询方向与关注列表相反，同样由 Mapper 一次性组装 VO。
    return baseMapper.getFollowerList( userId );
  }

  @Override
  public long getFollowingCount( Long userId ) {
    // 关注数以当前有效的关注记录为准，排除已经取消关注的逻辑删除数据。
    LambdaQueryWrapper < UserFollow > wrapper = new LambdaQueryWrapper <>();
    wrapper.eq( UserFollow :: getFollowerId, userId )
        .eq( UserFollow :: getIsDeleted, 0 );
    return count( wrapper );
  }

  @Override
  public long getFollowerCount( Long userId ) {
    // 粉丝数统计的是有多少有效记录把该用户作为 followingId，即有多少人正在关注该用户。
    LambdaQueryWrapper < UserFollow > wrapper = new LambdaQueryWrapper <>();
    wrapper.eq( UserFollow :: getFollowingId, userId )
        .eq( UserFollow :: getIsDeleted, 0 );
    return count( wrapper );
  }

  @Override
  public boolean isFollowing( Long followerId, Long followingId ) {
    // 关注状态统一使用 followerId + followingId + 未删除三个条件判断，
    // 保证关注、防重、取消关注和前端状态展示使用同一套口径。
    LambdaQueryWrapper < UserFollow > wrapper = new LambdaQueryWrapper <>();
    wrapper.eq( UserFollow :: getFollowerId, followerId )
        .eq( UserFollow :: getFollowingId, followingId )
        .eq( UserFollow :: getIsDeleted, 0 );
    return count( wrapper ) > 0;
  }

  @Override
  public Page < TravelNoteVO > getFollowingFeed( Integer pageNum, Integer pageSize ) {
    // 关注动态流只能查看当前登录用户关注的人发布的游记，因此 userId 必须从登录态获取。
    Long userId = StpUtil.getLoginIdAsLong();

    // MyBatis-Plus 的 Page 对象会被 Mapper 填充分页数据，服务层只负责创建分页参数并返回结果。
    Page < TravelNoteVO > page = new Page <>( pageNum, pageSize );
    return baseMapper.getFollowingFeed( page, userId );
  }
}
