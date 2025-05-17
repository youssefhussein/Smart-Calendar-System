package com.schedule.calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringVersion;

@SpringBootApplication
public class CalendarApplication {
    public final Logger logger = LoggerFactory.getLogger(CalendarApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(CalendarApplication.class, args);
    }
    
}
