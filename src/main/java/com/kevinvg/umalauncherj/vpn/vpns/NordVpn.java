package com.kevinvg.umalauncherj.vpn.vpns;

import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.vpn.Vpn;
import com.kevinvg.umalauncherj.vpn.VpnManager;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class NordVpn extends Vpn {
    public NordVpn(VpnManager vpnManager) {
        super(vpnManager);
    }

    @Override
    protected boolean uniqueConnect() {
        String exePath = vpnManager.settings.get(AppSettings.SettingKey.VPN_CLIENT_PATH);
        if (exePath == null || exePath.isBlank()) {
            vpnManager.ui.showErrorDialog("NordVPN requires the VPN Executable setting to be filled!");
            return false;
        }

        File executable = new File(exePath);
        if (!executable.exists()) {
            vpnManager.ui.showErrorDialog("NordVPN executable not found at location %s".formatted(exePath));
            return false;
        }

        try {
            Runtime.getRuntime().exec(new String[]{executable.getAbsolutePath(), "-c", "-g", "Japan"});
        } catch (Exception e) {
            log.error("Failed to start NordVPN", e);
            return false;
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    protected void uniqueDisconnect() {
        String exePath = vpnManager.settings.get(AppSettings.SettingKey.VPN_CLIENT_PATH);
        if (exePath == null || exePath.isBlank()) {
            vpnManager.ui.showErrorDialog("NordVPN requires the VPN Executable setting to be filled!");
            return;
        }

        try {
            Runtime.getRuntime().exec(new String[]{new File(exePath).getAbsolutePath(), "-d"});
        } catch (Exception e) {
            log.error("Failed to disconnect NordVPN", e);
            return;
        }
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void afterIpCheck() {
        this.uniqueConnect();
    }
}
