package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.entity.UserFollow;
import com.travel.vo.TravelNoteVO;
import com.travel.vo.UserFollowVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserFollowMapper extends BaseMapper < UserFollow > {

  // 复活软删除的关注记录，原生 SQL 绕过 MP 全局软删除拦截器
  @Update ( "UPDATE user_follow SET is_deleted = 0 WHERE follower_id = #{followerId} AND following_id = #{followingId} AND is_deleted != 0" )
  int restoreFollow( @Param ( "followerId" ) Long followerId, @Param ( "followingId" ) Long followingId );

  // 查询某个用户的关注列表（我关注了哪些人），带对方的昵称头像
  List < UserFollowVO > getFollowingList( @Param ( "userId" ) Long userId );

  // 查询某个用户的粉丝列表（谁关注了我），带对方的昵称头像
  List < UserFollowVO > getFollowerList( @Param ( "userId" ) Long userId );

  // Feed流，查询我关注的所有人发布的游记，分页
  // 返回类型必须是 IPage（Page 实现了 IPage），MP 才会把结果 setRecords 回填到 page 对象
  Page < TravelNoteVO > getFollowingFeed(
      @Param ( "page" ) Page < TravelNoteVO > page,
      @Param ( "userId" ) Long userId
  );

}
