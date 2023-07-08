package com.nhan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CosmeticsStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(CosmeticsStoreApplication.class, args);
	}

}
