package com.kevinvg.umalauncherj.helpertable.domain;

import java.util.List;

public enum ParamType {
    UNKNOWN("UNKNOWN"),
    SPEED("SPD"),
    STAMINA("STA"),
    POWER("POW"),
    GUTS("GUT"),
    WIT("WIT"),
    ENERGY("NRG"),
    SKILLPT("SKL");

    public static final List<ParamType> STANDARD_TYPES = List.of(
            SPEED,
            STAMINA,
            POWER,
            GUTS,
            WIT
    );

    public final String displayName;
    ParamType(String displayName) {
        this.displayName = displayName;
    }
}
