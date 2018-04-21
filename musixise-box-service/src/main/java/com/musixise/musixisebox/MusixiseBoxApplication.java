package com.musixise.musixisebox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableCaching
@ComponentScan
@EnableAutoConfiguration
public class MusixiseBoxApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusixiseBoxApplication.class, args);
	}
}
