package com.kevinvg.umalauncherj.vpn;

import com.kevinvg.umalauncherj.rest.client.IpService;
import com.kevinvg.umalauncherj.rest.client.IpServiceBackup;
import com.kevinvg.umalauncherj.rest.client.VpnService;
import com.kevinvg.umalauncherj.rest.client.domain.IpData;
import com.kevinvg.umalauncherj.rest.client.domain.VpnData;
import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import com.kevinvg.umalauncherj.tray.TrayIconController;
import com.kevinvg.umalauncherj.ui.UmaUiManager;
import io.quarkus.runtime.Shutdown;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Singleton
public class VpnManager {
    public final AppSettingsManager settings;
    public final UmaUiManager ui;
    protected final TrayIconController tray;
    private List<VpnData> serverList = new ArrayList<>();
    private Vpn vpn;

    @RestClient
    VpnService vpnService;
    @RestClient
    IpService ipService;
    @RestClient
    IpServiceBackup ipServiceBackup;

    @Inject
    public VpnManager(AppSettingsManager settings, UmaUiManager ui, TrayIconController tray) {
        this.settings = settings;
        this.ui = ui;
        this.tray = tray;
    }

    public void connect() {
        if (!settings.<Boolean>get(AppSettings.SettingKey.VPN_ENABLED)) {
            return;
        }

        String vpnSetting = settings.get(AppSettings.SettingKey.VPN_CLIENT);
        if (vpnSetting == null) {
            log.error("Something went wrong with getting VPN_CLIENT");
            return;
        }

        try {
            vpn = VpnType.valueOf(vpnSetting.toUpperCase()).vpnClass.getConstructor(VpnManager.class).newInstance(this);
        } catch (Exception e) {
            log.error("Could not create instance of VPN client!", e);
            ui.showErrorDialog("Could not create instance of VPN client!");
            return;
        }

        boolean success = false;
        try {
            success = vpn.connect();
        } catch (Exception e) {
            log.error("Exception connecting to vpn", e);
        }

        if (!success) {
            ui.showErrorDialog("Failed to connect to VPN.");
        }
    }

    public void disconnect() {
        if (vpn == null) {
            return;
        }

        vpn.disconnect();
        vpn = null;
    }

    @Shutdown
    void shutdown() {
        log.info("Shutting down VPNManager");
        disconnect();
    }

    public String determineVpngateProfile() {
        if (serverList.isEmpty()) {
            log.info("Requesting VPN server list from Umapyoi.net");

            List<VpnData> newList;
            if (Boolean.TRUE.equals(settings.<Boolean>get(AppSettings.SettingKey.VPN_DMM_ONLY))) {
                newList = vpnService.getDmmVpns();
            } else {
                newList = vpnService.getCygamesVpns();
            }

            if (!newList.isEmpty()) {
                serverList = newList.subList(0, Math.min(5, newList.size()));
                Collections.shuffle(serverList);
            }

        }

        if (serverList.isEmpty()) {
            return null;
        }
        var chosenServer = serverList.getFirst();
        if (serverList.size() > 1) {
            serverList.removeFirst();
        }
        return chosenServer._profile();
    }

    String getIp() {
        String ip = null;
        for (int tries = 0; tries < 10; tries++) {
            String tmpIp;
            if (tries < 0) {
                tmpIp = ipService.getIp().ip();
            } else {
                tmpIp = ipServiceBackup.getIp().ip();
            }
            if (tmpIp != null){
                ip = tmpIp;
                break;
            }
        }
        return ip;
    }
}
