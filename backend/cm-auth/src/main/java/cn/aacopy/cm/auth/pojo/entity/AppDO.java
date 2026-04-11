package cn.aacopy.cm.auth.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 应用
 */
@Data
@TableName(value = "app")
public class AppDO {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 认证组id
     */
    @TableField(value = "auth_group_id")
    private Long authGroupId;

    /**
     * 名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 名称
     */
    @TableField(value = "`logo`")
    private String logo;

    /**
     * 客户端id
     */
    @TableField(value = "client_id")
    private String clientId;

    /**
     * 客户端密钥
     */
    @TableField(value = "client_secret")
    private String clientSecret;

    /**
     * 重定向地址
     */
    @TableField(value = "redirect_uris")
    private String redirectUris;

    /**
     * Access Token过期时间（秒）
     */
    @TableField(value = "access_expires")
    private Long accessExpires;

    /**
     * Refresh Token过期时间（秒）
     */
    @TableField(value = "refresh_expires")
    private Long refreshExpires;

    /**
     * 状态：Y启用，N禁用
     */
    @TableField(value = "`status`")
    private String status;

    /**
     * 是否删除
     */
    @TableField(value = "deleted")
    private String deleted;

    /**
     * 创建人
     */
    @TableField(value = "create_by",fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(value = "update_by",fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}