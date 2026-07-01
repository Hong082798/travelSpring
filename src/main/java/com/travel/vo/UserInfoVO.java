package com.travel.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 当前登录用户信息的输出对象。
 * <p>
 * 设计要点——这是一道安全边界：
 * 1. 【没有 password】——密码哈希永远不出后端。这是 VO 最重要的职责。
 * 2. 【没有 isDeleted】——逻辑删除标记是内部实现细节，前端不需要也不该知道。
 * 3. 【有 roles】——角色码列表，前端用它做权限相关的 UI 控制。
 * <p>
 * 为什么不直接复用列表用的 UserVO：
 * UserVO 是"管理员看别人"的列表项，没有 roles；
 * UserInfoVO 是"我看我自己"，需要 roles 来驱动前端权限。
 * 语义不同，分开定义，各自演化互不干扰。
 */
@Data
public class UserInfoVO {

  private Long id;
  private String username;
  private String nickname;
  private String avatar;
  private String phone;
  private Integer gender;
  private Integer status;
  private LocalDateTime createTime;

  // 当前用户拥有的角色，如["admin"]或者["user"]
  private List < String > roles;

}
