package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.travel.entity.Comment;
import com.travel.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper < Comment > {

  @Select ( "SELECT c.id, c.user_id, c.content, c.rating, c.create_time, " +
      "u.nickname, u.avatar " +
      "FROM comment c " +
      "LEFT JOIN user u ON c.user_id = u.id " +
      "WHERE c.scenic_id = #{scenicId} AND c.is_deleted = 0 " +
      "ORDER BY c.create_time DESC" )
  List < CommentVO > getCommentByScenicId( @Param ( "scenicId" ) Long scenicId );

  IPage < CommentVO > selectCommentPage( IPage < CommentVO > page, @Param ( "keyword" ) String keyword );
}
