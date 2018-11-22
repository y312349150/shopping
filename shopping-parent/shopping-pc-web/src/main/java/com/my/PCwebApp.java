package com.my;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
@EnableFeignClients
@SpringBootApplication
@EnableEurekaClient
public class PCwebApp {

	public static void main(String[] args) {
		SpringApplication.run(PCwebApp.class,args);
	}

}
