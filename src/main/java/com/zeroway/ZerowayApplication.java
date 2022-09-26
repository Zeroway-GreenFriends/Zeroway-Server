package com.zeroway;

import com.zeroway.aop.LogAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@Import(LogAspect.class)
public class ZerowayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZerowayApplication.class, args);
	}

}
