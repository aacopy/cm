package cn.aacopy.cm.auth.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 客户端信息
 */
@Data
@TableName(value = "client")
public class ClientDO {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

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
     * 授权码模式使用的重定向地址列表（逗号分隔）
     */
    @TableField(value = "redirect_uris")
    private String redirectUris;

    /**
     * 用户授权同意
     */
    @TableField(value = "consent")
    private Integer consent;

    /**
     * 应用id
     */
    @TableField(value = "app_id")
    private Long appId;

    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(value = "update_by")
    private Long updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableField(value = "deleted")
    private Integer deleted;

    public static final String COL_ID = "id";

    public static final String COL_CLIENT_ID = "client_id";

    public static final String COL_CLIENT_SECRET = "client_secret";

    public static final String COL_REDIRECT_URIS = "redirect_uris";

    public static final String COL_CONSENT = "consent";

    public static final String COL_APP_ID = "app_id";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_BY = "update_by";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_DELETED = "deleted";
}