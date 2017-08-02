package com.kishlaly.utils.taas.services.impl;

import com.kishlaly.utils.taas.services.VirtualizationService;
import org.springframework.stereotype.Service;

/**
 * @author Vladimir Kishlaly
 * @since 02.08.2017
 */
@Service
public class KVMService implements VirtualizationService {

    @Override
    public String start(String macAddress) {
        return null;
    }

    @Override
    public String stop(String macAddress) {
        return null;
    }

    @Override
    public String getStatus(String macAddress) {
        return null;
    }
}
