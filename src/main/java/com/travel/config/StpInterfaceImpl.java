package com.travel.config;

import cn.dev33.satoken.stp.StpInterface;
import com.travel.service.RoleService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Sa-Token 权限数据源——告诉框架"某用户拥有哪些角色/权限"。
 * <p>
 * 工作原理（务必理解这个回调机制）：
 * 当某接口标了 @SaCheckRole("admin")，请求进来时，
 * Sa-Token 会【自动调用】下面的 getRoleList(loginId, ...)，
 * 把当前登录用户的 id 作为 loginId 传进来；
 * 我们返回该用户的角色码列表（如 ["admin"]）；
 * Sa-Token 拿这个列表和注解要求的 "admin" 比对，
 * 命中则放行，不命中则抛 NotRoleException → 被全局异常处理器转成 403。
 * <p>
 * 我们【只负责提供数据】，校验和拦截全由框架自动完成——这就是框架的价值。
 *
 * @Component：交给 Spring 容器管理，Sa-Token 会自动找到这个 Bean。
 * 少了这个注解，Sa-Token 找不到实现，@SaCheckRole 会失效或报错。
 */
@Component
public class StpInterfaceImpl implements StpInterface {

  private final RoleService roleService;

  // 构造器注入 RoleService
  public StpInterfaceImpl( RoleService roleService ) {
    this.roleService = roleService;
  }

  /**
   * 返回某账号的角色码列表。
   *
   * @param o
   *     当前登录用户的标识（就是我们 StpUtil.login(userId) 时传的 userId）
   * @param s
   *     登录类型，多端登录时区分用，单端场景忽略即可
   * @return 角色码列表，如 ["admin"]
   */
  @Override
  public List < String > getRoleList( Object o, String s ) {

    // loginId是Object类型，登录是用的Long类型的userId
    Long userId = Long.valueOf( o.toString() );

    return roleService.getRoleCodesByUserId( userId );
  }

  /**
   * 返回某账号的权限码列表。
   * 我们用的是方案 B（只到角色层，没有细粒度权限），
   * 所以这里返回空列表即可。将来升级到方案 C 再在此查权限。
   */
  @Override
  public List < String > getPermissionList( Object o, String s ) {
    return Collections.emptyList();
  }

}
