package cn.aacopy.cm.auth.controller;

import cn.aacopy.cm.auth.common.Result;
import cn.aacopy.cm.auth.model.dto.AppDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cmyang
 * @date 2025/10/26
 */
@Tag(name = "应用管理")
@RestController
@RequestMapping("/v1/app")
public class AppController {


    @Operation(summary = "保存应用")
    @PostMapping("/save")
    public Result<Boolean> save(@Validated @RequestBody AppDTO appDTO) {
        return Result.success(true);
    }
}
