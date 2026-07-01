package com.travel.controller;

import com.travel.common.Result;
import com.travel.entity.ScenicCategory;
import com.travel.service.ScenicCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag ( name = "景点分类管理", description = "景点分类相关的API接口" )
@RestController
@RequestMapping ( "/api/scenic-categories" )
public class ScenicCategoryController {

  @Autowired
  private ScenicCategoryService scenicCategoryService;

  @Operation ( summary = "查询所有分类" )
  @GetMapping ( "/list" )
  public Result < List < ScenicCategory > > list() {
    List < ScenicCategory > categories = scenicCategoryService.list();
    return Result.success( categories );
  }

  @Operation ( summary = "根据ID查询分类" )
  @GetMapping ( "/{id}" )
  public Result < ScenicCategory > getById( @PathVariable Long id ) {
    ScenicCategory category = scenicCategoryService.getById( id );
    return Result.success( category );
  }

  @Operation ( summary = "新增分类" )
  @PostMapping
  public Result < Void > add( @RequestBody ScenicCategory category ) {
    scenicCategoryService.save( category );
    return Result.success();
  }

  @Operation ( summary = "更新分类" )
  @PutMapping
  public Result < Void > update( @RequestBody ScenicCategory category ) {
    scenicCategoryService.updateById( category );
    return Result.success();
  }

  @Operation ( summary = "删除分类(逻辑删除)" )
  @DeleteMapping ( "/{id}" )
  public Result < Void > delete( @PathVariable Long id ) {
    scenicCategoryService.removeById( id );
    return Result.success();
  }
}
