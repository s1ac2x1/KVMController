package com.kishlaly.utils.taas.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vladimir Kishlaly
 * @since 02.08.2017
 */
@RestController
public class ErrorsController implements org.springframework.boot.autoconfigure.web.ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error() {
        return "Usage: /api/vm/<start/stop/status>/<MAC>";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }


}
