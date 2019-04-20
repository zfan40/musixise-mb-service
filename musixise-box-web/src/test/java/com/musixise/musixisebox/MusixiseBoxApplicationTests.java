package com.musixise.musixisebox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@SpringBootApplication(scanBasePackages = {"com.musixise"})
public class MusixiseBoxApplicationTests {


	public static void main(String[] args) {
		SpringApplication.run(MusixiseBoxApplicationTests.class, args);
	}

	@Configuration
	@Profile("test")
	@ComponentScan(lazyInit = true)
	static class LocalConfig {
	}

}
