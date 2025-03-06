package com.kevinvg.umalauncherj.helpertable.domain;

public enum Column {
    FACILITY("Facility"),
    SPEED("Speed"),
    STAMINA("Stamina"),
    POWER("Power"),
    GUTS("Guts"),
    WIT("Wit"),
    SS_MATCH("SS Match");

    public final String facilityName;

    Column(String facilityName) {
        this.facilityName = facilityName;
    }
}
