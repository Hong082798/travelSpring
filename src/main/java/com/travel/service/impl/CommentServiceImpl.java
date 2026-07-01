package com.travel.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travel.dto.CommentDTO;
import com.travel.entity.Comment;
import com.travel.entity.ScenicSpot;
import com.travel.exception.BusinessException;
import com.travel.mapper.CommentMapper;
import com.travel.service.CommentService;
import com.travel.service.ScenicSpotService;
import com.travel.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl < CommentMapper, Comment > implements CommentService {

  @Autowired
  private ScenicSpotService scenicSpotService;

  @Override
  public void addComment( Long scenicId, CommentDTO commentDTO ) {

    // 获取当前登录用户ID
    Long userId = StpUtil.getLoginIdAsLong();

    // 保存评论
    Comment comment = new Comment();
    comment.setUserId( userId );
    comment.setScenicId( scenicId );
    comment.setContent( commentDTO.getContent() );
    comment.setRating( commentDTO.getRating() );
    this.save( comment );

    // 重新计算该景点的平均分
    updateScenicScore( scenicId );

  }

  @Override
  public List < CommentVO > getCommentByScenicId( Long scenicId ) {
    return this.baseMapper.getCommentByScenicId( scenicId );
  }

  @Override
  public IPage < CommentVO > getCommentPage( Integer pageNum, Integer pageSize, String keyword ) {
    IPage < CommentVO > page = new Page <>( pageNum, pageSize );
    return baseMapper.selectCommentPage( page, keyword );
  }

  @Override
  public void deleteComment( Long id ) {

    Comment comment = getById( id );
    if ( comment == null ) {
      throw new BusinessException( "评论不存在" );
    }
    removeById( id );

  }

  // 重新计算并更新景点的平均分
  private void updateScenicScore( Long scenicId ) {

    // 查询该景点所有未删除的评论
    List < Comment > comments = this.lambdaQuery().eq( Comment :: getScenicId, scenicId ).list();
    if ( comments.isEmpty() ) {
      return;
    }

    // 计算评分总和
    int sum = 0;
    for ( Comment c : comments ) {
      sum += c.getRating();
    }

    // 计算平均分，保留1位小数
    BigDecimal average = new BigDecimal( sum ).divide( new BigDecimal( comments.size() ), 1,
        RoundingMode.HALF_UP );

    // 更新景点的score 字段
    ScenicSpot scenicSpot = new ScenicSpot();
    scenicSpot.setId( scenicId );
    scenicSpot.setScore( average );
    scenicSpotService.updateById( scenicSpot );

  }
}
