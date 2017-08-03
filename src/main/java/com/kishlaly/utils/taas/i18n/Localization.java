package com.kishlaly.utils.taas.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author Vladimir Kishlaly
 * @since 03.08.2017
 */
@Component
public class Localization {

    @Autowired
    private MessageSource source;

    public String get(LocalizationKeys key) {
        return source.getMessage(key.name().toLowerCase(), null, key.getDefaultMessage(), Locale.getDefault());
    }

    public String get(LocalizationKeys key, Object[] args) {
        return source.getMessage(key.name().toLowerCase(), args, key.getDefaultMessage(), Locale.getDefault());
    }

}
