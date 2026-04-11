CREATE DATABASE IF NOT EXISTS `cm_auth` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE `cm_auth`.`user` (
                        `id` bigint(20) NOT NULL COMMENT 'ID',
                        `auth_group_id` bigint(20) DEFAULT NULL COMMENT '认证组id',
                        `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
                        `nickname` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '昵称',
                        `avatar` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
                        `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT 'N' COMMENT '状态：Y启用，N禁用',
                        `deleted` char(1) COLLATE utf8mb4_unicode_ci DEFAULT 'N' COMMENT '是否删除',
                        `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
                        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
                        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`),
                        KEY `user_email_IDX` (`email`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

CREATE TABLE `cm_auth`.`auth_group` (
                              `id` bigint(20) NOT NULL COMMENT 'ID',
                              `name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '认证组名称',
                              `deleted` char(1) COLLATE utf8mb4_unicode_ci DEFAULT 'N' COMMENT '是否删除',
                              `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
                              `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
                              `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='认证组';

CREATE TABLE `cm_auth`.`app` (
                       `id` bigint(20) NOT NULL COMMENT 'ID',
                       `auth_group_id` bigint(20) DEFAULT NULL COMMENT '认证组',
                       `name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
                       `logo` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'logo',
                       `client_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '客户端id',
                       `client_secret` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '客户端密钥',
                       `redirect_uris` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '重定向地址',
                       `access_expires` bigint(20) DEFAULT NULL COMMENT 'Access Token过期时间（秒）',
                       `refresh_expires` bigint(20) DEFAULT NULL COMMENT 'Refresh Token过期时间（秒）',
                       `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT 'N' COMMENT '状态：Y启用，N禁用',
                       `deleted` char(1) COLLATE utf8mb4_unicode_ci DEFAULT 'N' COMMENT '是否删除',
                       `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
                       `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                       `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
                       `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                       PRIMARY KEY (`id`),
                       KEY `app_client_id_IDX` (`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='应用';