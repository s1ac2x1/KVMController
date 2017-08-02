package com.kishlaly.utils.taas.services;

import com.kishlaly.utils.taas.exceptions.ConnectionException;
import org.libvirt.Connect;
import org.libvirt.LibvirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Vladimir Kishlaly
 * @since 02.08.2017
 */
public enum KVMConnection {

    INSTANCE;

    private final Logger LOG = LoggerFactory.getLogger(KVMConnection.class);
    private ConcurrentHashMap<String, Connect> pool = new ConcurrentHashMap<>();

    public Connect get(String url) {
        Connect connection = pool.get(url);
        if (connection != null) {
            return connection;
        }
        try {
            connection = new Connect(url);
            pool.put(url, connection);
        } catch (LibvirtException e) {
            throw new ConnectionException("Failed to establish connection with " + url);
        } finally {
            return connection;
        }
    }

    public void close(String url) {
        close(url, pool.get(url));
    }

    public void closeAll() {
        pool.forEach((url, connection) -> close(url, connection));
        pool.clear();
    }

    private void close(String url, Connect connection) {
        if (connection != null) {
            try {
                connection.close();
                pool.remove(url);
            } catch (LibvirtException e) {
                LOG.warn("Failed to close connection with " + url, e);
            }
        }
    }

}
