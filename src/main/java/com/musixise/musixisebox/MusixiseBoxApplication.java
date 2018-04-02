package com.musixise.musixisebox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class MusixiseBoxApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusixiseBoxApplication.class, args);
	}
}
