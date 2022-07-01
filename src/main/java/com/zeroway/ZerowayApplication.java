package com.zeroway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ZerowayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZerowayApplication.class, args);
	}

}
