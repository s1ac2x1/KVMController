package com.kishlaly.utils.taas;

import com.kishlaly.utils.taas.services.KVMConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextStoppedEvent;

/**
 * @author Vladimir Kishlaly
 * @since 02.08.2017
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.kishlaly.utils.taas"})
@PropertySource("classpath:application.properties")
public class Application implements ApplicationListener<ContextStoppedEvent> {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void onApplicationEvent(ContextStoppedEvent event) {
        KVMConnection.INSTANCE.closeAll();
    }
}
