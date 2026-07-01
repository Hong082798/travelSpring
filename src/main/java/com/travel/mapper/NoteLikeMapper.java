package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.entity.NoteLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface NoteLikeMapper extends BaseMapper < NoteLike > {

  // 用原生 SQL 复活软删除的点赞记录，绕过 MP 全局软删除拦截器。
  // 注意：Wrapper 方式（哪怕直接调 baseMapper.update）仍会被自动追加 AND is_deleted=0，
  // 导致永远匹配不到 is_deleted=1 的历史行，只有原生 SQL 才能真正命中并复活。
  @Update ( "UPDATE note_like SET is_deleted = 0 WHERE user_id = #{userId} AND note_id = #{noteId} AND is_deleted != 0" )
  int restoreLike(
      @Param ( "userId" ) Long userId,
      @Param ( "noteId" ) Long noteId
  );
}
