package com.kishlaly.utils.taas.services;

/**
 * @author Vladimir Kishlaly
 * @since 02.08.2017
 */
public interface VirtualizationService {

    String start(String macAddress);

    String stop(String macAddress);

    String getStatus(String macAddress);

}
