package com.travel.controller;

import com.travel.common.Result;
import com.travel.service.FavoriteService;
import com.travel.vo.EntertainmentItemVO;
import com.travel.vo.ScenicSpotVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag ( name = "收藏管理", description = "景点 / 玩乐收藏相关接口（需要登录）" )
@RestController
@RequestMapping ( "/api/favorites" )
public class FavoriteController {

  @Autowired
  private FavoriteService favoriteService;

  @Operation ( summary = "收藏（targetType=scenic/entertainment）" )
  @PostMapping ( "/{targetType}/{targetId}" )
  public Result < Void > add( @PathVariable String targetType, @PathVariable Long targetId ) {

    favoriteService.addFavorite( targetType, targetId );
    return Result.success();

  }

  @Operation ( summary = "取消收藏（targetType=scenic/entertainment）" )
  @DeleteMapping ( "/{targetType}/{targetId}" )
  public Result < Void > cancelFavorite( @PathVariable String targetType, @PathVariable Long targetId ) {

    favoriteService.cancelFavorite( targetType, targetId );
    return Result.success();

  }

  @Operation ( summary = "检查是否已经收藏（targetType=scenic/entertainment）" )
  @GetMapping ( "/check/{targetType}/{targetId}" )
  public Result < Boolean > check( @PathVariable String targetType, @PathVariable Long targetId ) {

    boolean favorites = favoriteService.isFavorites( targetType, targetId );
    return Result.success( favorites );

  }

  // 思考题落点：景点 Tab 与玩乐 Tab 各自返回完整且不同的 VO，于是拆成两条独立路由，
  // 每条返回各自确定的类型，既保留编译期类型安全，也让 Swagger 能给出准确的响应结构。
  @Operation ( summary = "查询我收藏的景点列表" )
  @GetMapping ( "/my/scenic" )
  public Result < List < ScenicSpotVO > > myFavoriteScenics() {

    List < ScenicSpotVO > favorites = favoriteService.getMyFavoriteScenics();
    return Result.success( favorites );

  }

  @Operation ( summary = "查询我收藏的玩乐列表" )
  @GetMapping ( "/my/entertainment" )
  public Result < List < EntertainmentItemVO > > myFavoriteEntertainments() {

    List < EntertainmentItemVO > favorites = favoriteService.getMyFavoriteEntertainments();
    return Result.success( favorites );

  }

}
