package cn.aacopy.cm.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author cmyang
 * @date 2025/10/26
 */
@Schema(description = "认证组")
@Data
public class AuthGroupDTO {

    @Schema(description = "认证组ID")
    private Long id;
    @Schema(description = "认证组编码")
    private String code;
    @Schema(description = "认证组名称")
    private String name;
    @Schema(description = "认证组描述")
    private String description;
    @Schema(description = "状态，0-启用，1-禁用")
    private Integer status;
}
