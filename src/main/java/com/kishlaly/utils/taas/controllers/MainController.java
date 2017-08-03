package com.kishlaly.utils.taas.controllers;

import com.kishlaly.utils.taas.i18n.Localization;
import com.kishlaly.utils.taas.services.VirtualizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

import static com.kishlaly.utils.taas.i18n.LocalizationKeys.START_FAILED;
import static com.kishlaly.utils.taas.i18n.LocalizationKeys.STATUS_FAILED;
import static com.kishlaly.utils.taas.i18n.LocalizationKeys.STOP_FAILED;

/**
 * @author Vladimir Kishlaly
 * @since 02.08.2017
 */
@RestController
@RequestMapping(value = "${api.root}")
public class MainController {

    @Autowired
    private VirtualizationService virtualizationService;
    @Autowired
    private Localization i18n;

    @RequestMapping("${api.start}/{mac}")
    public String start(@PathVariable(value = "mac") String mac) {
        return action(() -> virtualizationService.start(orEmpty(mac)), i18n.get(START_FAILED));
    }

    @RequestMapping("${api.stop}/{mac}")
    public String stop(@PathVariable(value = "mac") String mac) {
        return action(() -> virtualizationService.stop(orEmpty(mac)), i18n.get(STOP_FAILED));
    }

    @RequestMapping("${api.status}/{mac}")
    public String getStatus(@PathVariable(value = "mac") String mac) {
        return action(() -> virtualizationService.getStatus(orEmpty(mac)), i18n.get(STATUS_FAILED));
    }

    private String action(Supplier<String> supplier, String msg) {
        String vmState = "";
        try {
            vmState = supplier.get();
        } catch (Exception e) {
            vmState = msg + ": " + e.getMessage();
        } finally {
            return vmState;
        }
    }

    private String orEmpty(String src) {
        return src != null ? src : "<empty>";
    }

}
