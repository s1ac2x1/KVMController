package com.kishlaly.utils.taas.controllers;

import com.kishlaly.utils.taas.services.VirtualizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

/**
 * @author Vladimir Kishlaly
 * @since 02.08.2017
 */
@RestController
@RequestMapping(value = "/api/vm/")
public class KVMController {

    @Autowired
    private VirtualizationService virtualizationService;

    public String start(@RequestParam String macAddress) {
        return action(() -> virtualizationService.start(macAddress), "Failed to start");
    }

    public String stop(@RequestParam String macAddress) {
        return action(() -> virtualizationService.stop(macAddress), "Failed to stop");
    }

    public String getStatus(@RequestParam String macAddress) {
        return action(() -> virtualizationService.getStatus(macAddress), "Failed to get VM status");
    }

    private String action(Supplier<String> supplier, String msg) {
        String vmState = "";
        try {
            vmState = supplier.get();
        } catch (Exception e) {
            vmState = msg + ": " + e.getLocalizedMessage();
        } finally {
            return vmState;
        }
    }

}
