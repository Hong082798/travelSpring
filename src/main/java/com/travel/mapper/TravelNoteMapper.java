package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.dto.TravelNoteQueryDTO;
import com.travel.entity.TravelNote;
import com.travel.vo.DateStatsVO;
import com.travel.vo.TravelNoteVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TravelNoteMapper extends BaseMapper < TravelNote > {

  Page < TravelNoteVO > pageQueryVO(
      @Param ( "page" ) Page < TravelNoteVO > page,
      @Param ( "dto" ) TravelNoteQueryDTO dto,
      @Param ( "userId" ) Long userId
  );

  @Select ( "SELECT DATE(create_time) AS date, COUNT(*) AS count " +
      "FROM travel_note " +
      "WHERE is_deleted = 0 " +
      "AND create_time >= #{startDate} " +
      "GROUP BY DATE(create_time) " +
      "ORDER BY date ASC" )
  List < DateStatsVO > countByDate( @Param ( "startDate" ) LocalDateTime startDate );

  TravelNoteVO getDetailById( @Param ( "id" ) Long id, @Param ( "userId" ) Long userId );

}
