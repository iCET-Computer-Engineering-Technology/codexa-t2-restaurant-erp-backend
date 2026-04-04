package edu.icet.ecom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application entry point for the Restaurant ERP Backend.
 *
 * EnableScheduling enables Spring's scheduled task support.
 * This is required for the AutomatedMessageSchedulerService to work.
 */
@SpringBootApplication
@EnableScheduling
public class Main {
    public static void main(String[] args){
        SpringApplication.run(Main.class, args);
    }
}
