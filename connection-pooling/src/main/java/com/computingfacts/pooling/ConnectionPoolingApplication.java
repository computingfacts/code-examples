package com.computingfacts.pooling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.computingfacts.pooling")
public class ConnectionPoolingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConnectionPoolingApplication.class, args);

    }

}
