package com.kishlaly.utils.taas.services.impl;

import com.kishlaly.utils.taas.annotations.Connection;
import com.kishlaly.utils.taas.exceptions.ConnectionException;
import com.kishlaly.utils.taas.exceptions.MACAddressException;
import com.kishlaly.utils.taas.i18n.Localization;
import com.kishlaly.utils.taas.i18n.LocalizationKeys;
import com.kishlaly.utils.taas.services.VirtualizationService;
import com.kishlaly.utils.taas.utils.MACAddressValidation;
import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.DomainInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.IntStream;

import static com.kishlaly.utils.taas.i18n.LocalizationKeys.CONNECTION_CLOSED;
import static com.kishlaly.utils.taas.i18n.LocalizationKeys.DOMAIN_BOOTING;
import static com.kishlaly.utils.taas.i18n.LocalizationKeys.DOMAIN_INACTIVE;
import static com.kishlaly.utils.taas.i18n.LocalizationKeys.DOMAIN_NOT_FOUND;
import static com.kishlaly.utils.taas.i18n.LocalizationKeys.DOMAIN_STARTED;
import static com.kishlaly.utils.taas.i18n.LocalizationKeys.DOMAIN_STARTING_ERROR;
import static com.kishlaly.utils.taas.i18n.LocalizationKeys.DOMAIN_STATUS_ERROR;
import static com.kishlaly.utils.taas.i18n.LocalizationKeys.DOMAIN_STOPPING_ERROR;
import static com.kishlaly.utils.taas.i18n.LocalizationKeys.ILLEGAL_MAC_ADDRESS;
import static com.kishlaly.utils.taas.i18n.LocalizationKeys.NO_ACTIVE_DOMAINS;
import static com.kishlaly.utils.taas.i18n.LocalizationKeys.PROVIDE_TEMPLATE;
import static com.kishlaly.utils.taas.i18n.LocalizationKeys.START_MAC_FAILED;
import static com.kishlaly.utils.taas.i18n.LocalizationKeys.STARTED;
import static com.kishlaly.utils.taas.i18n.LocalizationKeys.STOPPED;
import static com.kishlaly.utils.taas.i18n.LocalizationKeys.UNKNOWN_ERROR;
import static org.libvirt.DomainInfo.DomainState.VIR_DOMAIN_NOSTATE;
import static org.libvirt.DomainInfo.DomainState.VIR_DOMAIN_RUNNING;
import static org.libvirt.DomainInfo.DomainState.VIR_DOMAIN_SHUTOFF;

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
    @Value("${kvm.os.type}")
    private String osType;

    @Autowired
    private Localization i18n;

    @Override
    public String start(String macAddress) {
        validate(macAddress);

        String result = i18n.get(UNKNOWN_ERROR);
        String mac = "'" + macAddress + "'";
        String vmName = osType + "_" + macAddress;
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
                    return i18n.get(STARTED);
                }
            } else {
                for (int id : ids) {
                    Domain domain = connection.domainLookupByID(id);
                    if (domain.getName().trim().equalsIgnoreCase(vmName)) {
                        if (domain.isActive() == 1) {
                            result = i18n.get(DOMAIN_STARTED);
                        }
                        if (domain.isActive() == 0) {
                            result = i18n.get(DOMAIN_INACTIVE);
                        }
                        if (domain.isActive() == 1) {
                            result = i18n.get(UNKNOWN_ERROR);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new ConnectionException(i18n.get(DOMAIN_STARTING_ERROR), e);
        } finally {
            return result;
        }
    }

    @Override
    public String stop(String macAddress) {
        validate(macAddress);
        String result = i18n.get(NO_ACTIVE_DOMAINS);
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
                        result = i18n.get(STOPPED);
                    } else {
                        result = i18n.get(START_MAC_FAILED) + " " + macAddress;
                    }
                }
            } else if (definedDomains.length >= 1) {
                for (String definedDomName : definedDomains) {
                    Domain domain = connection.domainLookupByName(definedDomName);
                    String domainDesc = domain.getXMLDesc(0);
                    if (domainDesc.contains(macAddress)) {
                        domain.undefine();
                        result = i18n.get(DOMAIN_NOT_FOUND);
                    }
                }
            }
        } catch (Exception e) {
            throw new ConnectionException(i18n.get(DOMAIN_STOPPING_ERROR), e);
        } finally {
            return result;
        }
    }

    @Override
    public String getStatus(String macAddress) {
        validate(macAddress);
        String result = i18n.get(LocalizationKeys.DOMAIN_STARTED);
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
                        result = i18n.get(STARTED);
                    } else if (state == VIR_DOMAIN_SHUTOFF) {
                        result = i18n.get(STOPPED);
                    } else {
                        result = i18n.get(DOMAIN_BOOTING);
                    }
                }

            }
        } catch (Exception e) {
            throw new ConnectionException(i18n.get(DOMAIN_STATUS_ERROR), e);
        } finally {
            return result;
        }
    }

    private void validate(String macAddress) {
        if (!MACAddressValidation.isValid(macAddress.toUpperCase())) {
            throw new MACAddressException(i18n.get(ILLEGAL_MAC_ADDRESS) + ": " + macAddress);
        }
        if (connection == null) {
            throw new ConnectionException(i18n.get(CONNECTION_CLOSED));
        }
        if (windowsTemplate == null || windowsTemplate.isEmpty()) {
            throw new ConnectionException(i18n.get(PROVIDE_TEMPLATE, new Object[]{osType}));
        }
    }

}
