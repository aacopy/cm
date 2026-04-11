package cn.aacopy.cm.starter.mybatisplus;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * @author cmyang
 * @date 2026/1/31
 */
@AutoConfiguration(before = MybatisPlusAutoConfiguration.class)
@MapperScan("cn.aacopy.cm.**.mapper")
public class MyBatisPlusConfig {
}
