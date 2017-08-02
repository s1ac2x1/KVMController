package com.kishlaly.utils.taas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Vladimir Kishlaly
 * @since 02.08.2017
 */
@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"com.kishlaly.utils.taas"})
@PropertySource("classpath:application.properties")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
