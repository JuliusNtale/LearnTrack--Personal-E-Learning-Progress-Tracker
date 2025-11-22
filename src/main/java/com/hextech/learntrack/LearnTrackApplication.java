package com.hextech.learntrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync  // Enable async processing
@EnableScheduling  // Enable scheduled tasks for notifications
public class
LearnTrackApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LearnTrackApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(LearnTrackApplication.class, args);
    }
}