package com.me.learn.springcloud.resttemplateeurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ResttemplateEurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResttemplateEurekaServerApplication.class, args);
    }
}
