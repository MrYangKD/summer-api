package com.cn.summer.api;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.cn.summer.api.mapper.dataSource.DynamicDataSourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {SessionAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
@Import(DynamicDataSourceRegister.class)
public class SummerApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(SummerApiApplication.class, args);
	}
}
