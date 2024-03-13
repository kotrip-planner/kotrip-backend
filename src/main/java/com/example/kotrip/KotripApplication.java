package com.example.kotrip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class KotripApplication {

    public static void main(String[] args) {
        SpringApplication.run(KotripApplication.class, args);
    }

}
