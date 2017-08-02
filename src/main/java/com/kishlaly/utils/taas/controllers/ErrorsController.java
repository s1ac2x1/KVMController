package com.kishlaly.utils.taas.controllers;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Vladimir Kishlaly
 * @since 02.08.2017
 */
public class ErrorsController implements org.springframework.boot.autoconfigure.web.ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error() {
        return "";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }


}
