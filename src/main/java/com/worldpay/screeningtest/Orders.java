package com.worldpay.screeningtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@ComponentScan(basePackages = {"com.worldpay.screeningtest"})
@EnableScheduling
public class Orders {

    private static final Logger log = LoggerFactory.getLogger(Orders.class);

    public static void main(String[] args) {
        SpringApplication.run(Orders.class);
    }

}
