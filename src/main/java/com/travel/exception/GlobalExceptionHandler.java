package com.travel.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import com.travel.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler ( MethodArgumentNotValidException.class )
  public Result < Void > handleValidException( MethodArgumentNotValidException e ) {
    FieldError fieldError = e.getBindingResult().getFieldError();
    String message = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";
    log.warn( "参数校验异常: {}", message );
    return Result.error( 400, message );
  }

  @ExceptionHandler ( NotLoginException.class )
  public Result < Void > handleNotLoginException( NotLoginException e ) {
    log.warn( "未登录异常: {}", e.getMessage() );
    return Result.error( 401, "未登录，请先登录" );
  }

  @ExceptionHandler ( BusinessException.class )
  public Result < Void > handleBusinessException( BusinessException e ) {
    log.warn( "业务异常: {}", e.getMessage() );
    return Result.error( e.getCode(), e.getMessage() );
  }

  @ExceptionHandler ( NoResourceFoundException.class )
  public Result < Void > handleNoResourceFoundException( NoResourceFoundException e ) {
    return Result.error( 404, "资源不存在" );
  }

  @ExceptionHandler ( Exception.class )
  public Result < Void > handleException( Exception e ) {
    log.error( "系统异常：", e );
    return Result.error( 500, "系统异常，请稍后再试" );
  }

  // 已经登录但是无权限（角色不符）：返回403
  @ExceptionHandler ( NotRoleException.class )
  public Result < Void > handleNotRole( NotRoleException e ) {
    return Result.error( 403, "权限不足，需要管理员角色" );
  }

}
