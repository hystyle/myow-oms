package com.myow.firstmile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@EnableCaching
@SpringBootApplication(
        scanBasePackages = "com.myow",
        exclude = {DataSourceAutoConfiguration.class},
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
public class FirstmileApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(FirstmileApplication.class, args);
        String contextPath = context.getEnvironment().getProperty("server.servlet.context-path");
        System.out.println("Knife4j API Docs Path: " + contextPath + "/doc.html#/home");
    }
}
