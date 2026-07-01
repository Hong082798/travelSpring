package com.travel.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.travel.entity.UserFollow;
import com.travel.vo.UserFollowVO;
import com.travel.vo.TravelNoteVO;

import java.util.List;

public interface UserFollowService extends IService < UserFollow > {

  /** 关注某人 */
  void follow( Long followingId );

  /** 取消关注 */
  void unfollow( Long followingId );

  /** 我关注的人列表 */
  List < UserFollowVO > getFollowingList( Long userId );

  /** 我的粉丝列表 */
  List < UserFollowVO > getFollowerList( Long userId );

  /** 我关注了多少人 */
  long getFollowingCount( Long userId );

  /** 我有多少粉丝 */
  long getFollowerCount( Long userId );

  /** 我是否关注了某人 */
  boolean isFollowing( Long followerId, Long followingId );

  /** Feed流：我关注的人发的游记 */
  Page < TravelNoteVO > getFollowingFeed( Integer pageNum, Integer pageSize );
}