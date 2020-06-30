package com.vmarketing.core.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

//url:https://juejin.im/post/5ecfca676fb9a04793456fb8#heading-4
@Configuration
@EnableTransactionManagement
@MapperScan("com.vmarketing.mapper")
public class MybatisPlusConfig {
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		return paginationInterceptor;
	}
}
