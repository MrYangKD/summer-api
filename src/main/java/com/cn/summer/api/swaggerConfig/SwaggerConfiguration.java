package com.cn.summer.api.swaggerConfig;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * .
 * swagger访问类.
 * @author YangYK
 * @create: 2019-11-16 14:52
 * @since 1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket buildDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(Boolean.TRUE)
                .apiInfo(buildApiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo buildApiInfo(){
        return new ApiInfoBuilder()
                .title("操作数据统一接入平台")
                .description("文档说明：</br> "
                        + "本接口采用的是RSA非对称性验签，每个渠道的终端，都需要去前置库申请一个私钥作生成验签使用。</br>"
                        + "content参数：写入数据json串</br>"
                        + "bizType参数：业务类型。</br>")
                .version("1.0")
                .build();
    }
}
