package com.travel.service.impl;

import com.travel.mapper.ScenicSpotMapper;
import com.travel.mapper.TravelNoteMapper;
import com.travel.vo.CategoryStatsVO;
import com.travel.vo.DateStatsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travel.service.CommentService;
import com.travel.service.DashboardService;
import com.travel.service.ScenicSpotService;
import com.travel.service.TravelNoteService;
import com.travel.service.UserService;
import com.travel.vo.DashboardStatsVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service // 交给 Spring 管理，成为一个可被注入的 Bean
public class DashboardServiceImpl implements DashboardService {

  // 注入各个模块的 Service —— 复用它们的 count()，而不是自己写 SQL
  @Autowired
  private UserService userService;
  @Autowired
  private ScenicSpotService scenicSpotService;
  @Autowired
  private TravelNoteService travelNoteService;
  @Autowired
  private CommentService commentService;
  @Autowired
  private ScenicSpotMapper scenicSpotMapper;
  @Autowired
  private TravelNoteMapper travelNoteMapper;

  @Override
  public DashboardStatsVO getStats() {
    DashboardStatsVO vo = new DashboardStatsVO();
    // count() 是 MyBatis-Plus ServiceImpl 自带方法，
    // 它是"生成的 SQL"，自动带 is_deleted=0，数的是未删除的记录
    vo.setUserCount( userService.count() );
    vo.setScenicCount( scenicSpotService.count() );
    vo.setNoteCount( travelNoteService.count() );
    vo.setCommentCount( commentService.count() );
    return vo;
  }

  @Override
  public List < CategoryStatsVO > getCategoryStats() {
    return scenicSpotMapper.countByCategory();
  }

  @Override
  public List < DateStatsVO > getNoteTrend() {

    int days = 7;
    LocalDate today = LocalDate.now();

    // 起点：今天以后往后推六天（含七天）
    LocalDate startDay = today.minusDays( days - 1 );
    LocalDateTime startDataTime = startDay.atStartOfDay();

    // 查有游记的的那几天
    List < DateStatsVO > dbResult = travelNoteMapper.countByDate( startDataTime );
    // 把结果转换
    Map < String, Long > countMap = new HashMap <>();
    for ( DateStatsVO vo : dbResult ) {
      countMap.put( vo.getDate(), vo.getCount() );
    }

    // 升成完整的7天骨架
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );
    List < DateStatsVO > result = new ArrayList <>();
    for ( int i = 0; i < days; i++ ) {
      LocalDate day = startDay.plusDays( i );
      String dateStr = day.format( formatter );
      DateStatsVO vo = new DateStatsVO();
      vo.setDate( dateStr );
      vo.setCount( countMap.getOrDefault( dateStr, 0L ) );
      result.add( vo );
    }

    return result;

  }
}
