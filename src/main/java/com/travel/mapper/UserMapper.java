package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.dto.UserQueryDTO;
import com.travel.entity.User;
import com.travel.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface UserMapper extends BaseMapper < User > {

  Page < UserVO > pageQueryVO(
      @Param ( "page" ) Page < UserVO > page,
      @Param ( "dto" ) UserQueryDTO dto
  );

}
