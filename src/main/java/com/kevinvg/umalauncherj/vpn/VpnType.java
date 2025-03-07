package com.kevinvg.umalauncherj.vpn;

import com.kevinvg.umalauncherj.vpn.vpns.NordVpn;
import com.kevinvg.umalauncherj.vpn.vpns.OpenVpn;
import com.kevinvg.umalauncherj.vpn.vpns.SoftEther;

public enum VpnType {
    NORDVPN(NordVpn.class),
    OPENVPN(OpenVpn.class),
    SOFTETHER(SoftEther.class);

    public final Class<? extends Vpn> vpnClass;
    VpnType(Class<? extends Vpn> clazz) {
        this.vpnClass = clazz;
    }
}
