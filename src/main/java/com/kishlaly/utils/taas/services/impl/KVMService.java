package com.kishlaly.utils.taas.services.impl;

import com.kishlaly.utils.taas.annotations.Connection;
import com.kishlaly.utils.taas.exceptions.ConnectionException;
import com.kishlaly.utils.taas.exceptions.MACAddressException;
import com.kishlaly.utils.taas.services.VirtualizationService;
import com.kishlaly.utils.taas.utils.MACAddressValidation;
import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.DomainInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.IntStream;

import static com.kishlaly.utils.taas.utils.VMState.*;
import static org.libvirt.DomainInfo.DomainState.*;

/**
 * @author Vladimir Kishlaly
 * @since 02.08.2017
 */
@Service
public class KVMService implements VirtualizationService {

    @Connection(url = "qemu:///system")
    private Connect connection;

    @Value("${kvm.windows.template}")
    private String windowsTemplate;

    @Override
    public String start(String macAddress) {
        validate(macAddress);

        String result = UNKNOWN_ERROR.getMessage();
        String mac = "'" + macAddress + "'";
        String vmName = "windows_" + macAddress;
        try {
            int[] ids = IntStream.of(connection.listDomains()).filter(id -> id > 0).toArray();

            if (ids.length == 0) {
                UUID uuid = UUID.randomUUID();
                windowsTemplate = windowsTemplate.replace("$uUid", uuid.toString());
                windowsTemplate = windowsTemplate.replace("$vmName", vmName);
                windowsTemplate = windowsTemplate.replace("$vmMac", mac);

                Domain domain = connection.domainDefineXML(windowsTemplate);
                domain.create();
                if (domain.getInfo().state == VIR_DOMAIN_RUNNING) {
                    return "Started";
                }
            } else {
                for (int id : ids) {
                    Domain domain = connection.domainLookupByID(id);
                    if (domain.getName().trim().equalsIgnoreCase(vmName)) {
                        if (domain.isActive() == 1) {
                            result = RUNNING.getMessage();
                        }
                        if (domain.isActive() == 0) {
                            result = INACTIVE.getMessage();
                        }
                        if (domain.isActive() == 1) {
                            result = UNKNOWN_ERROR.getMessage();
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new ConnectionException("Error while starting the domain", e);
        } finally {
            return result;
        }
    }

    @Override
    public String stop(String macAddress) {
        validate(macAddress);
        String result = "No active domains";
        try {
            int[] runningDomains = connection.listDomains();
            String[] definedDomains = connection.listDefinedDomains();

            if (runningDomains.length >= 1) {
                for (int id : runningDomains) {
                    Domain domain = connection.domainLookupByID(id);
                    String domainDesc = domain.getXMLDesc(0);
                    if (domainDesc.contains(macAddress)) {
                        domain.shutdown();
                        domain.undefine();
                        result = "Stopped";
                    } else {
                        result = "Failed to find domain with MAC " + macAddress;
                    }
                }
            } else if (definedDomains.length >= 1) {
                for (String definedDomName : definedDomains) {
                    Domain domain = connection.domainLookupByName(definedDomName);
                    String domainDesc = domain.getXMLDesc(0);
                    if (domainDesc.contains(macAddress)) {
                        domain.undefine();
                        result = "Domain with specified MAC was not defined";
                    }
                }
            }
        } catch (Exception e) {
            throw new ConnectionException("Error while stopping the domain", e);
        } finally {
            return result;
        }
    }

    @Override
    public String getStatus(String macAddress) {
        validate(macAddress);
        String result = null;
        try {
            int[] runningDomains = connection.listDomains();
            if (runningDomains.length < 1) {
                result = VIR_DOMAIN_NOSTATE.name();
            }
            for (int id : runningDomains) {
                Domain domain = connection.domainLookupByID(id);
                String domainDesc = domain.getXMLDesc(0);
                if (domainDesc.contains(macAddress)) {
                    DomainInfo.DomainState state = domain.getInfo().state;
                    if (state == VIR_DOMAIN_RUNNING) {
                        result = STARTED.getMessage();
                    } else if (state == VIR_DOMAIN_SHUTOFF) {
                        result = STOPPED.getMessage();
                    } else {
                        result = BOOTING.getMessage();
                    }
                }

            }
        } catch (Exception e) {
            throw new ConnectionException("Error while getting domain status", e);
        } finally {
            return result;
        }
    }

    private void validate(String macAddress) {
        if (!MACAddressValidation.isValid(macAddress.toUpperCase())) {
            throw new MACAddressException("Illegal MAC Address: " + macAddress);
        }
        if (connection == null) {
            throw new ConnectionException("Connection closed");
        }
        if (windowsTemplate == null || windowsTemplate.isEmpty()) {
            throw new ConnectionException("Please, provide template for KVM Windows image");
        }
    }

}
