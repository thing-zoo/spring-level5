package com.example.springlevel5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableJpaAuditing
@SpringBootApplication
public class SpringLevel5Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringLevel5Application.class, args);
    }

}
