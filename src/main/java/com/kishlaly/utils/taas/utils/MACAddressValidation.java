package com.kishlaly.utils.taas.utils;

import java.util.regex.Pattern;

/**
 * @author Vladimir Kishlaly
 * @since 02.08.2017
 */
public class MACAddressValidation {

    private static Pattern pattern = Pattern.compile("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");

    public static boolean isValid(String macAddress) {
        return pattern.matcher(macAddress).find();
    }

}
