package com.russo.roma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:roma.properties")
public class RomaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RomaApplication.class, args);
	}

}
