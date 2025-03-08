package com.kevinvg.umalauncherj.helpertable.domain;

public enum CommandType {
    UNKNOWN("unknown"),
    SPEED("speed"),
    STAMINA("stamina"),
    POWER("power"),
    GUTS("guts"),
    WIT("wiz"),
    SS_MATCH("ss_match");

    public final String internalName;

    CommandType(String internalName) {
        this.internalName = internalName;
    }
}