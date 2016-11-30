package com.chu;

import com.chu.util.filter.CustomFilter;
import com.chu.util.listerner.CustomListerner;
import com.chu.util.servlet.CustomServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;

import javax.servlet.MultipartConfigElement;

@EnableJms
@SpringBootApplication
public class DemoApplication {
	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		return new ServletRegistrationBean(new CustomServlet(), "/ServletDemo");
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		return new FilterRegistrationBean(new CustomFilter(), servletRegistrationBean());
	}

	@Bean
	public ServletListenerRegistrationBean<CustomListerner> servletListenerRegistrationBean() {
		return new ServletListenerRegistrationBean<CustomListerner>(new CustomListerner());
	}



	/**
	 * 文件上传临时路径
	 */
	@Bean
	MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setLocation("c:/app/tmp");
		return factory.createMultipartConfig();
	}
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
