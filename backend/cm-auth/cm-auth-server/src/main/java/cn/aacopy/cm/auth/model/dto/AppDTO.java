package cn.aacopy.cm.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 保存应用对象
 * @author cmyang
 * @date 2025/10/26
 */
@Schema(description = "保存应用对象")
@Data
public class AppDTO {

    @Schema(description = "应用ID")
    private Long id;
    @Schema(description = "应用名称")
    private String name;
    @Schema(description = "应用描述")
    private String description;
    @Schema(description = "认证分组ID")
    private Long authGroupId;
}
