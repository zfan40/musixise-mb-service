package com.musixise.musixisebox;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication(scanBasePackages = {"com.musixise"})
public class MusixiseBoxApplicationTests {


	public static void main(String[] args) {
		SpringApplication.run(MusixiseBoxApplication.class, args);
	}

	@Configuration
	@Profile("test")
	@ComponentScan(lazyInit = true)
	static class LocalConfig {
	}

	@Test
	public void testEnpty() {}
}
