package com.musixise.musixisebox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.MultipartConfigElement;
import java.io.File;

@SpringBootApplication(scanBasePackages = {"com.musixise"})
@EnableAsync
@EnableScheduling
@EnableCaching
public class MusixiseBoxApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusixiseBoxApplication.class, args);
	}

	@Bean
	MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		String location = System.getProperty("user.dir") + "/tmp";
		System.out.println("user.dir " + location);
		File tmpFile = new File(location);
		if (!tmpFile.exists()) {
			tmpFile.mkdirs();
		}
		factory.setLocation(location);
		return factory.createMultipartConfig();
	}
}
