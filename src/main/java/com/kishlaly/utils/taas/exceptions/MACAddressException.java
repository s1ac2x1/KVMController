package com.kishlaly.utils.taas.exceptions;

/**
 * @author Vladimir Kishlaly
 * @since 02.08.2017
 */
public class MACAddressException extends RuntimeException {

    public MACAddressException() {
    }

    public MACAddressException(String message) {
        super(message);
    }

    public MACAddressException(String message, Throwable cause) {
        super(message, cause);
    }

    public MACAddressException(Throwable cause) {
        super(cause);
    }

    public MACAddressException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
