package cn.aacopy.cm.tool.controller;

import cn.aacopy.cm.starter.web.pojo.Result;
import cn.aacopy.cm.starter.web.pojo.enums.YesNoEnum;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cmyang
 * @date 2026/2/22
 */
@Tag(name = "生成")
@RestController
@RequestMapping("/generate")
public class GenerateController {

    @Operation(summary = "雪花算法ID")
    @GetMapping("/snowflake")
    public Result<Long> snowflake() {
        return Result.success(IdUtil.getSnowflakeNextId());
    }

    @Operation(summary = "UUID")
    @GetMapping("/uuid")
    public Result<String> uuid(@RequestParam YesNoEnum withHyphen) {
        if (YesNoEnum.Y == withHyphen) {
            return Result.success(IdUtil.fastUUID());
        } else {
            return Result.success(IdUtil.fastSimpleUUID());
        }
    }

    @Operation(summary = "随机字符串")
    @GetMapping("/randomString")
    public Result<String> randomString(@RequestParam Integer length) {
        return Result.success(RandomUtil.randomString(length));
    }
}
