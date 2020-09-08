package com.computingfacts.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication(scanBasePackages = {"uk.ac.ebi","com.computingfacts.api"})
public class HypermediaDrivenApiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(HypermediaDrivenApiApplication.class, args);
	}

}
