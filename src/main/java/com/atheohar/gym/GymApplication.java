package com.atheohar.gym;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GymApplication {

  @Value("${webdriver.firefox.bin}")
  private static String firefoxLocation;

  public static void main(String[] args) {
    SpringApplication.run(GymApplication.class, args);
  }
}
