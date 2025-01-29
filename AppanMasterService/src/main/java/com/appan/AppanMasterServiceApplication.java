package com.appan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(considerNestedRepositories = true)
public class AppanMasterServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppanMasterServiceApplication.class, args);
		System.out.println("Server Start...");
	}
}