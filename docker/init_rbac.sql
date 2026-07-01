-- ============================================================
-- RBAC 初始化脚本（方案 B：user / role / user_role 三表）
-- 执行一次即可。在 Navicat 选中 travel_platform 库运行。
-- ============================================================

-- ---------- 1. 角色表 ----------
-- 存"系统里有哪些角色"。code 是程序里用的标识（如 admin），name 是给人看的中文名。
CREATE TABLE IF NOT EXISTS `role`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `code`        VARCHAR(50) NOT NULL COMMENT '角色编码（程序用，如 admin/user）',
    `name`        VARCHAR(50) NOT NULL COMMENT '角色名称（给人看，如 管理员）',
    `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT     NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删 1已删',
    PRIMARY KEY (`id`),
    -- code 唯一：同一个角色编码不能重复。加唯一索引既约束数据又加速按 code 查。
    UNIQUE KEY `uk_code` (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色表';

-- ---------- 2. 用户-角色关联表（中间表，体现多对多）----------
-- 一行 = "某用户拥有某角色"。一个用户多个角色 = 多行；一个角色多个用户 = 多行。
-- 这就是"多对多"在数据库里的标准落地：用一张中间表拆成两个一对多。
CREATE TABLE IF NOT EXISTS `user_role`
(
    `id`          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`     BIGINT   NOT NULL COMMENT '用户ID',
    `role_id`     BIGINT   NOT NULL COMMENT '角色ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_deleted`  TINYINT  NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删 1已删',
    PRIMARY KEY (`id`),
    -- 同一个用户不能重复绑定同一个角色，唯一约束防重。
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    -- 按 user_id 查角色是高频操作（每次鉴权都要查），单独建索引加速。
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户角色关联表';

-- ---------- 3. 插入两个基础角色 ----------
-- 用 INSERT ... 防重：先查不存在才插，避免重复执行脚本时报唯一键冲突。
INSERT INTO `role` (`code`, `name`)
SELECT 'admin', '管理员'
WHERE NOT EXISTS (SELECT 1 FROM `role` WHERE `code` = 'admin');

INSERT INTO `role` (`code`, `name`)
SELECT 'user', '普通用户'
WHERE NOT EXISTS (SELECT 1 FROM `role` WHERE `code` = 'user');

-- ---------- 4. 把 hong 设为管理员 ----------
-- 关键：用子查询按 username='hong' 和 code='admin' 动态取 id，
-- 这样不依赖"hong 的 id 一定是几号""admin 角色 id 一定是几号"，更稳健。
-- 同样用 NOT EXISTS 防重复绑定。
INSERT INTO `user_role` (`user_id`, `role_id`)
SELECT u.id, r.id
FROM `user` u,
     `role` r
WHERE u.username = 'hong'
  AND r.code = 'admin'
  AND NOT EXISTS (SELECT 1
                  FROM `user_role` ur
                  WHERE ur.user_id = u.id
                    AND ur.role_id = r.id);
