package com.kishlaly.utils.taas.controllers;

import com.kishlaly.utils.taas.services.VirtualizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

/**
 * @author Vladimir Kishlaly
 * @since 02.08.2017
 */
@RestController
@RequestMapping(value = "/api/vm")
public class KVMController {

    @Autowired
    private VirtualizationService virtualizationService;

    @RequestMapping("/start/{macAddress}")
    public String start(@PathVariable(value = "macAddress") String macAddress) {
        return action(() -> virtualizationService.start(orEmpty(macAddress)), "Failed to start");
    }

    @RequestMapping("/stop/{macAddress}")
    public String stop(@PathVariable(value = "macAddress") String macAddress) {
        return action(() -> virtualizationService.stop(orEmpty(macAddress)), "Failed to stop");
    }

    @RequestMapping("/status/{macAddress}")
    public String getStatus(@PathVariable(value = "macAddress") String macAddress) {
        return action(() -> virtualizationService.getStatus(orEmpty(macAddress)), "Failed to get VM status");
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

    private String orEmpty(String src) {
        return src != null ? src : "<empty>";
    }

}
