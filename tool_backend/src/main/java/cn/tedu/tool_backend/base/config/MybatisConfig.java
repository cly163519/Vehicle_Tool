package cn.tedu.tool_backend.base.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("cn.tedu.tool_backend.*.mapper")
public class MybatisConfig {
}
