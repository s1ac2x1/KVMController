package com.kishlaly.utils.taas.spring;

import com.kishlaly.utils.taas.annotations.Connection;
import com.kishlaly.utils.taas.services.KVMConnection;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * @author Vladimir Kishlaly
 * @since 02.08.2017
 */
@Component
public class ConnectionBPP implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        for (Field field : bean.getClass().getDeclaredFields()) {
            Connection annotation = field.getAnnotation(Connection.class);
            if (annotation != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, bean, KVMConnection.INSTANCE.get(annotation.url()));
            }
        }
        return bean;
    }

}
