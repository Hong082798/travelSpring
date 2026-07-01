package com.travel.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.travel.common.Result;
import com.travel.dto.ScenicSpotQueryDTO;
import com.travel.entity.ScenicSpot;
import com.travel.service.ScenicSpotService;
import com.travel.vo.ScenicSpotVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag ( name = "景点管理", description = "景点的增删改查与分页查询" )
@RestController
@RequestMapping ( "/api/scenic-spots" )
public class ScenicSpotController {

  @Autowired
  private ScenicSpotService scenicSpotService;

  @Operation ( summary = "分页查询景点", description = "支持按分类查询，按名称模糊搜索" )
  @PostMapping ( "/page" )
  public Result < IPage < ScenicSpotVO > > page( @RequestBody ScenicSpotQueryDTO queryDTO ) {
    IPage < ScenicSpotVO > page = scenicSpotService.pageQuery( queryDTO );
    return Result.success( page );
  }

  @Operation ( summary = "根据ID查询景点详情" )
  @GetMapping ( "/{id}" )
  public Result < ScenicSpot > getById( @PathVariable Long id ) {
    ScenicSpot spot = scenicSpotService.getById( id );
    return Result.success( spot );
  }

  @Operation ( summary = "新增景点" )
  @PostMapping
  public Result < Void > add( @RequestBody ScenicSpot spot ) {
    scenicSpotService.save( spot );
    return Result.success();
  }

  @Operation ( summary = "修改景点" )
  @PutMapping
  public Result < Void > update( @RequestBody ScenicSpot spot ) {
    scenicSpotService.updateById( spot );
    return Result.success();
  }

  @Operation ( summary = "删除景点（软删除）" )
  @DeleteMapping ( "/{id}" )
  public Result < Void > delete( @PathVariable Long id ) {
    scenicSpotService.removeById( id );
    return Result.success();
  }

  @Operation ( summary = "查询景点详情（含分类名）" )
  @GetMapping ( "/detail/{id}" )
  public Result < ScenicSpotVO > detail( @PathVariable Long id ) {
    ScenicSpotVO vo = scenicSpotService.getDetailById( id );
    return Result.success( vo );
  }

}
