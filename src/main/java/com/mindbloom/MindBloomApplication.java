package com.mindbloom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * MindBloom – A compassionate AI-powered mental wellness platform.
 *
 * "We are not here to fix you. We are here to walk alongside you."
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class MindBloomApplication {

    public static void main(String[] args) {
        SpringApplication.run(MindBloomApplication.class, args);
    }
}
