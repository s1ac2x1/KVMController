package com.kishlaly.utils.taas.i18n;

/**
 * @author Vladimir Kishlaly
 * @since 02.08.2017
 */
public enum LocalizationKeys {

    STARTED("Started"),
    STOPPED("Stopped"),

    START_FAILED("Failed to start"),
    START_MAC_FAILED("Failed to find domain with MAC"),
    STOP_FAILED("Failed to stop"),

    STATUS_FAILED("Failed to get VM status"),
    DOMAIN_STARTING_ERROR("Error while starting the domain"),
    DOMAIN_STOPPING_ERROR("Error while stopping the domain"),
    DOMAIN_STATUS_ERROR("Error while getting domain status"),
    DOMAIN_INACTIVE("Domain is inactive"),
    UNKNOWN_ERROR("Unknown error"),
    DOMAIN_STARTED("Up and running"),
    DOMAIN_BOOTING("Booting"),
    DOMAIN_NOT_FOUND("Domain with specified MAC was not defined"),
    ILLEGAL_MAC_ADDRESS("Illegal MAC Address"),
    CONNECTION_CLOSED("Connection closed"),
    PROVIDE_TEMPLATE("Please, provide template for KVM image"),
    NO_ACTIVE_DOMAINS("No active domains");

    private String defaultMessage;

    LocalizationKeys(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
