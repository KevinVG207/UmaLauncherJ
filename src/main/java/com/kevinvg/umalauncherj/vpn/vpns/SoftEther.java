package com.kevinvg.umalauncherj.vpn.vpns;

import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.vpn.Vpn;
import com.kevinvg.umalauncherj.vpn.VpnManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class SoftEther extends Vpn {
    public SoftEther(VpnManager vpnManager) {
        super(vpnManager);
    }

    @Override
    protected boolean uniqueConnect() {
        String ip = "";
        String override = vpnManager.settings.get(AppSettings.SettingKey.VPN_OVERRIDE_STRING);
        if (override != null && !override.isBlank()) {
            ip = override;
        } else {
            String profile = vpnManager.determineVpngateProfile();
            if (profile == null || profile.isBlank()) {
                return false;
            }

            String tmpIp = "";
            String tmpPort = "";
            for (String line : profile.split("\n")) {
                line = line.trim();
                if (line.isBlank() || line.startsWith("#") || !line.startsWith("remote ")) {
                    continue;
                }

                var splitLines = line.split(" ");

                if (splitLines.length < 3) {
                    continue;
                }

                tmpIp = splitLines[1];
                tmpPort = splitLines[2];
                break;
            }

            if (tmpIp.isBlank() || tmpPort.isBlank()) {
                return false;
            }

            ip = tmpIp + ":" + tmpPort;
        }

        if (ip.isBlank()) {
            return false;
        }

        log.info("Connecting SoftEther {}", ip);
        List<String[]> cmdList = List.of(
                new String[] {"vpncmd", "/CLIENT", "localhost", "/CMD", "AccountDisconnect", "uma_tmp"},
                new String[] {"vpncmd", "/CLIENT", "localhost", "/CMD", "AccountDelete", "uma_tmp"},
                new String[] {"vpncmd", "/CLIENT", "localhost", "/CMD", "AccountCreate", "uma_tmp", "/SERVER:%s".formatted(ip), "/USERNAME:vpn", "/HUB:VPNGATE", "/NICNAME:VPN"},
                new String[] {"vpncmd", "/CLIENT", "localhost", "/CMD", "AccountStatusHide", "uma_tmp"},
                new String[] {"vpncmd", "/CLIENT", "localhost", "/CMD", "AccountConnect", "uma_tmp"}
        );

        Runtime rt = Runtime.getRuntime();
        for (String[] cmd : cmdList) {
            try {
                log.info("Executing {}", Arrays.toString(cmd));
                rt.exec(cmd).waitFor();
            } catch (Exception e) {
                log.error("Failed to execute command {}", Arrays.toString(cmd));
                return false;
            }
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

    }
}
