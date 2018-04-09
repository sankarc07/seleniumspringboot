package com.selenium.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.selenium" })
public class SeleniumSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeleniumSpringBootApplication.class, args);
	}
}
