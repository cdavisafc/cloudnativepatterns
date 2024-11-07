package com.corneliadavis.cloudnative.connectionsposts.workflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PostAggregatorWorker {

    public static void main(String[] args) {
        SpringApplication.run(PostAggregatorWorker.class, args);
    }
}
