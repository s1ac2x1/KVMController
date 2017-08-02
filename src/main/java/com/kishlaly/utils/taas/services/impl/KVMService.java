package com.kishlaly.utils.taas.services.impl;

import com.kishlaly.utils.taas.exceptions.MACAddressException;
import com.kishlaly.utils.taas.services.VirtualizationService;
import com.kishlaly.utils.taas.utils.MACAddressValidation;
import org.springframework.stereotype.Component;

/**
 * @author Vladimir Kishlaly
 * @since 02.08.2017
 */
@Component
public class KVMService implements VirtualizationService {

    @Override
    public String start(String macAddress) {
        validate(macAddress);
        return "Started";
    }

    @Override
    public String stop(String macAddress) {
        validate(macAddress);
        return "Stopped";
    }

    @Override
    public String getStatus(String macAddress) {
        validate(macAddress);
        return "Up and running";
    }

    private void validate(String macAddress) {
        if (!MACAddressValidation.isValid(macAddress.toUpperCase())) {
            throw new MACAddressException("Illegal MAC Address: " + macAddress);
        }
    }

}
