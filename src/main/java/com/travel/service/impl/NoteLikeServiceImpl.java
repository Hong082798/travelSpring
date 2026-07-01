package com.travel.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travel.entity.NoteLike;
import com.travel.entity.TravelNote;
import com.travel.exception.BusinessException;
import com.travel.mapper.NoteLikeMapper;
import com.travel.mapper.TravelNoteMapper;
import com.travel.service.NoteLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoteLikeServiceImpl extends ServiceImpl < NoteLikeMapper, NoteLike > implements NoteLikeService {

  @Autowired
  private TravelNoteMapper travelNoteMapper;

  @Override
  @Transactional ( rollbackFor = Exception.class )
  public void like( Long noteId ) {

    // 校验游记存在且已经发布
    TravelNote note = travelNoteMapper.selectById( noteId );
    if ( note == null || note.getIsDeleted() == 1 ) {
      throw new BusinessException( 404, "游记不存在" );
    }

    // 获取当前登录用户
    Long userId = StpUtil.getLoginIdAsLong();

    // 防重，检查是否已点赞
    if ( isLiked( noteId, userId ) ) {
      throw new BusinessException( 400, "您已经点赞了！" );
    }

    // 先尝试把之前软删除的记录复活（避免唯一约束冲突）
    // 必须用原生 SQL：Wrapper 方式（含 baseMapper.update）会被 MP 软删除拦截器
    // 自动追加 AND is_deleted=0，永远匹配不到 is_deleted=1 的历史行。
    boolean restored = baseMapper.restoreLike( userId, noteId ) > 0;

    if ( !restored ) {
      // 历史上从未点过赞，新增一条
      NoteLike noteLike = new NoteLike();
      noteLike.setUserId( userId );
      noteLike.setNoteId( noteId );
      save( noteLike );
    }

    // like_count + 1
    LambdaUpdateWrapper < TravelNote > wrapper = new LambdaUpdateWrapper <>();
    wrapper.eq( TravelNote :: getId, noteId ).setSql( "like_count = like_count + 1" );
    travelNoteMapper.update( null, wrapper );

  }

  @Override
  @Transactional ( rollbackFor = Exception.class )
  public void cancelLike( Long noteId ) {

    // 校验游记存在
    TravelNote note = travelNoteMapper.selectById( noteId );
    if ( note == null || note.getIsDeleted() == 1 ) {
      throw new BusinessException( 404, "游记不存在" );
    }

    // 获取当前登录用户
    Long userId = StpUtil.getLoginIdAsLong();

    // 检查是否已点赞，没点赞不能取消
    boolean alreadyLiked = isLiked( noteId, userId );
    if ( !alreadyLiked ) {
      throw new BusinessException( 400, "您尚未点赞，不能取消" );
    }

    // 删除点赞记录
    LambdaUpdateWrapper < NoteLike > wrapper = new LambdaUpdateWrapper <>();
    wrapper.eq( NoteLike :: getUserId, userId ).eq( NoteLike :: getNoteId, noteId );
    remove( wrapper );

    // like_count - 1，加 AND like_count > 0 防止出现负数
    LambdaUpdateWrapper < TravelNote > noteWrapper = new LambdaUpdateWrapper <>();
    noteWrapper.eq( TravelNote :: getId, noteId ).gt( TravelNote :: getLikeCount, 0 ).setSql( "like_count = " +
        "like_count - 1" );
    travelNoteMapper.update( null, noteWrapper );

  }
  
  // 私有方法isLiked
  @Override
  public boolean isLiked( Long noteId, Long userId ) {
    LambdaQueryWrapper < NoteLike > wrapper = new LambdaQueryWrapper <>();
    wrapper.eq( NoteLike :: getUserId, userId )
        .eq( NoteLike :: getNoteId, noteId )
        .eq( NoteLike :: getIsDeleted, 0 );
    return count( wrapper ) > 0;
  }
}
