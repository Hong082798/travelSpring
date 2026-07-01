package com.travel.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.travel.common.Result;
import com.travel.dto.EntertainmentItemAddDTO;
import com.travel.dto.EntertainmentItemQueryDTO;
import com.travel.dto.EntertainmentItemUpdateDTO;
import com.travel.service.EntertainmentItemService;
import com.travel.vo.EntertainmentItemVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag ( name = "玩乐项目管理", description = "玩乐项目的增删改查与分页查询" )
@RestController
@RequestMapping ( "/api/entertainment-items" )
public class EntertainmentItemController {

  @Autowired
  private EntertainmentItemService entertainmentItemService;

  @Operation ( summary = "分页查询玩乐项目", description = "支持按分类、状态、关键词查询" )
  @PostMapping ( "/page" )
  public Result < IPage < EntertainmentItemVO > > page( @RequestBody EntertainmentItemQueryDTO queryDTO ) {
    IPage < EntertainmentItemVO > page = entertainmentItemService.pageQuery( queryDTO );
    return Result.success( page );
  }

  @Operation ( summary = "查询玩乐项目详情" )
  @GetMapping ( "/detail/{id}" )
  public Result < EntertainmentItemVO > detail( @PathVariable Long id ) {
    EntertainmentItemVO vo = entertainmentItemService.getDetailById( id );
    return Result.success( vo );
  }

  @Operation ( summary = "新增玩乐项目" )
  @PostMapping
  public Result < Void > add( @Valid @RequestBody EntertainmentItemAddDTO dto ) {
    entertainmentItemService.addItem( dto );
    return Result.success();
  }

  @Operation ( summary = "修改玩乐项目" )
  @PutMapping
  public Result < EntertainmentItemVO > update( @Valid @RequestBody EntertainmentItemUpdateDTO dto ) {
    EntertainmentItemVO vo = entertainmentItemService.updateItem( dto );
    return Result.success( vo );
  }

  @Operation ( summary = "修改玩乐项目状态", description = "status：0下架，1上架" )
  @PutMapping ( "/{id}/status/{status}" )
  public Result < EntertainmentItemVO > updateStatus( @PathVariable Long id, @PathVariable Integer status ) {
    EntertainmentItemVO vo = entertainmentItemService.updateStatus( id, status );
    return Result.success( vo );
  }

  @Operation ( summary = "删除玩乐项目" )
  @DeleteMapping ( "/{id}" )
  public Result < Void > delete( @PathVariable Long id ) {
    entertainmentItemService.removeById( id );
    return Result.success();
  }
}
