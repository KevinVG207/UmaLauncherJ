package com.kevinvg.umalauncherj.vpn.vpns;

import com.kevinvg.umalauncherj.vpn.Vpn;
import com.kevinvg.umalauncherj.vpn.VpnManager;

public class OpenVpn extends Vpn {
    public OpenVpn(VpnManager vpnManager) {
        super(vpnManager);
    }

    @Override
    protected boolean uniqueConnect() {
        return false;
    }

    @Override
    protected void uniqueDisconnect() {

    }
}
