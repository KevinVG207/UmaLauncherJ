package com.kevinvg.umalauncherj.vpn;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class Vpn {
    private static final long TIMEOUT_NANOS = 120L * 1_000_000_000L;
    private static final long CONNECTION_TIMEOUT_NANOS = 30L * 1_000_000_000L;

    protected final VpnManager vpnManager;

    protected Vpn(VpnManager vpnManager) {
        this.vpnManager = vpnManager;
    }

    public final boolean connect() throws InterruptedException {
        log.info("Connecting to VPN");
        vpnManager.tray.setConnecting();
        String beforeIp = vpnManager.getIp();
        long checkStartNanos = System.nanoTime();
        boolean totalSuccess = false;
        while (System.nanoTime() - checkStartNanos < TIMEOUT_NANOS) {
            boolean success = false;
            try {
                success = this.uniqueConnect();
            } catch (Exception e) {
                log.error("Failed to use VPN Client", e);
                vpnManager.ui.showErrorDialog("Failed to use VPN Client.");
                this.disconnect();
                return false;
            }

            if (!success) {
                this.disconnect();
                break;
            }

            long connectStartNanos = System.nanoTime();
            while (System.nanoTime() - connectStartNanos < CONNECTION_TIMEOUT_NANOS) {
                String afterIp = vpnManager.getIp();
                if (!afterIp.equals(beforeIp)) {
                    totalSuccess = true;
                    break;
                }
                this.afterIpCheck();
                Thread.sleep(2000);
            }

            if (totalSuccess) {
                break;
            }

            this.disconnect();
        }

        if (!totalSuccess) {
            log.error("Failed to connect to VPN server");
            vpnManager.ui.showErrorDialog("Failed to connect to VPN server.");
            this.disconnect();
            return false;
        }

        log.info("Successfully connected to VPN server");
        Thread.sleep(4000);
        vpnManager.tray.setConnected();
        return true;
    }

    protected final void disconnect() {
        try {
            this.uniqueDisconnect();
        } catch (Exception e) {
            log.error("Failed to disconnect VPN Client", e);
        }

        vpnManager.tray.resetStatus();
        log.info("Disconnected from VPN server");
    }

    protected abstract boolean uniqueConnect();

    protected abstract void uniqueDisconnect();

    protected void afterIpCheck() {
    }
}
