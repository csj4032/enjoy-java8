package com.genius.reactive.rxPubsubApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class TemperatureApplication implements AsyncConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(TemperatureApplication.class);
	}
}