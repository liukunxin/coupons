package com.quincy.coupons_merchants;

import com.quincy.coupons_merchants.security.AuthCheckInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;

@SpringBootApplication
public class CouponsMerchantsApplication extends WebMvcConfigurationSupport{
	//此处配置文件也可以单独做一个config类，然后修改为初始化的application即可
	@Resource
	private AuthCheckInterceptor authCheckInterceptor;

	public static void main(String[] args) {
		SpringApplication.run(CouponsMerchantsApplication.class, args);
	}
	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		// 注册拦截器,并配置拦截请求url
		registry.addInterceptor(authCheckInterceptor).addPathPatterns("/merchants/**");
		super.addInterceptors(registry);
	}
}

