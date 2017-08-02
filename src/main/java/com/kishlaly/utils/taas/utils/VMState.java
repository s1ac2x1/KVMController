package com.kishlaly.utils.taas.utils;

/**
 * @author Vladimir Kishlaly
 * @since 02.08.2017
 */
public enum VMState {

    RUNNING("Domain is already running"),
    INACTIVE("Domain is inactive"),
    UNKNOWN_ERROR("Unknown error"),
    STARTED("Up and running"),
    STOPPED("Stopped"),
    BOOTING("Booting");

    private String message;

    VMState(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
