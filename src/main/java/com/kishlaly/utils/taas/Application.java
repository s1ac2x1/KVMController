package com.kishlaly.utils.taas;

import com.kishlaly.utils.taas.services.KVMConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * @author Vladimir Kishlaly
 * @since 02.08.2017
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.kishlaly.utils.taas"})
@PropertySource("classpath:application.properties")
public class Application implements ApplicationListener<ContextStoppedEvent> {

    public static void main(String[] args) {
        System.setProperty("jna.library.path", "/usr/lib/");
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void onApplicationEvent(ContextStoppedEvent event) {
        KVMConnection.INSTANCE.closeAll();
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("i18/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
