package com.ibm.onservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = { "com.ibm.csaservice", "com.ibm.controller" })
public class OnServiceApplication {

	public static void main(String[] args) {

		SpringApplication springApplication = new SpringApplication(OnServiceApplication.class);
		springApplication.addListeners(new ApplicationPidFileWriter("onservice.pid"));
		springApplication.run(args);
	}
}
