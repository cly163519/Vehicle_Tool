package cn.tedu.tool_backend.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@Component
@EnableSwagger2WebMvc
public class Knife4jConfig {
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                /*
                  在 Java 中，return 确实是用于返回一个值。不过在链式调用（method chaining）的情况下，
                  返回值本身也可以是一个对象，通过该对象可以继续调用其方法。
                  这种方式常用于构建器模式（Builder Pattern），使代码更简洁和易读。
                 */
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.tedu.tool_backend"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("智慧车辆项目")
                .description("智慧车辆项目在线API文档")
                .version("1.0")
                .build();
    }
}
