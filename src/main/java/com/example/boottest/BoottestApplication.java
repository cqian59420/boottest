package com.example.boottest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BoottestApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoottestApplication.class, args);
    }
}
