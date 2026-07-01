package com.travel.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.travel.common.Result;
import com.travel.entity.EntertainmentCategory;
import com.travel.service.EntertainmentCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag ( name = "玩乐分类管理", description = "玩乐分类相关接口" )
@RestController
@RequestMapping ( "/api/entertainment-categories" )
public class EntertainmentCategoryController {

  @Autowired
  private EntertainmentCategoryService entertainmentCategoryService;

  @Operation ( summary = "查询启用的玩乐分类" )
  @GetMapping ( "/list" )
  public Result < List < EntertainmentCategory > > list() {
    LambdaQueryWrapper < EntertainmentCategory > wrapper = new LambdaQueryWrapper <>();
    wrapper.eq( EntertainmentCategory :: getStatus, 1 )
        .orderByAsc( EntertainmentCategory :: getSort )
        .orderByDesc( EntertainmentCategory :: getCreateTime );

    List < EntertainmentCategory > list = entertainmentCategoryService.list( wrapper );
    return Result.success( list );
  }

  @Operation ( summary = "根据ID查询玩乐分类" )
  @GetMapping ( "/{id}" )
  public Result < EntertainmentCategory > getById( @PathVariable Long id ) {
    EntertainmentCategory category = entertainmentCategoryService.getById( id );
    return Result.success( category );
  }

  @Operation ( summary = "新增玩乐分类" )
  @PostMapping
  public Result < Void > add( @RequestBody EntertainmentCategory category ) {
    entertainmentCategoryService.save( category );
    return Result.success();
  }

  @Operation ( summary = "修改玩乐分类" )
  @PutMapping
  public Result < Void > update( @RequestBody EntertainmentCategory category ) {
    entertainmentCategoryService.updateById( category );
    return Result.success();
  }

  @Operation ( summary = "删除玩乐分类" )
  @DeleteMapping ( "/{id}" )
  public Result < Void > delete( @PathVariable Long id ) {
    entertainmentCategoryService.removeById( id );
    return Result.success();
  }
}
