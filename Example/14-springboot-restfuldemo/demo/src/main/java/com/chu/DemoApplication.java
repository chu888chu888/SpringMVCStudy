package com.chu;

import com.chu.util.filter.CustomFilter;
import com.chu.util.listerner.CustomListerner;
import com.chu.util.servlet.CustomServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

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
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
