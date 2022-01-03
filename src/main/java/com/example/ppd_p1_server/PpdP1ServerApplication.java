package com.example.ppd_p1_server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

@EnableScheduling
@SpringBootApplication
public class PpdP1ServerApplication {
    @Autowired
    private ApplicationContext context;

    @Scheduled(fixedDelay = 2, initialDelay = 2, timeUnit = TimeUnit.MINUTES)
    public void shutdown() {
        int exitCode = SpringApplication.exit(context, () -> 0);
        System.exit(exitCode);
    }

    public static void main(String[] args) {
        SpringApplication.run(PpdP1ServerApplication.class, args);
    }

}
