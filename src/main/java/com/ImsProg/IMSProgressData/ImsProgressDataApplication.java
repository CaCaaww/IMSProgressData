package com.ImsProg.IMSProgressData;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ImsProgressDataApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(ImsProgressDataApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ImsProgressDataApplication.class);
	}

}
