package cn.aacopy.cm.auth.controller;

import cn.aacopy.cm.auth.common.Result;
import cn.aacopy.cm.auth.model.dto.AuthGroupDTO;
import cn.aacopy.cm.auth.service.AuthGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author cmyang
 * @date 2025/10/26
 */
@Tag(name = "认证组")
@RestController
@RequestMapping("/v1/authGroup")
public class AuthGroupController {

    @Resource
    private AuthGroupService authGroupService;

    @Operation(summary = "保存认证组")
    @PostMapping("/save")
    public Result<Boolean> save(@Validated @RequestBody AuthGroupDTO authGroupDTO) {
        return Result.success(authGroupService.save(authGroupDTO));
    }
}
