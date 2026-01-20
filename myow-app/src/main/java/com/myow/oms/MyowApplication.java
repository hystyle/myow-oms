package com.myow.oms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

@EnableCaching
@SpringBootApplication(scanBasePackages = "com.myow", exclude = {DataSourceAutoConfiguration.class})
public class MyowApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MyowApplication.class, args);

        String property = context.getEnvironment().getProperty("server.servlet.context-path");

        System.out.println("Knife4j API Docs Path: " + property + "/doc.html#/home");
    }
}

