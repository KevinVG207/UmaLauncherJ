package com.kevinvg.umalauncherj.mdb.domain;

public enum SupportCardType {
    UNKNOWN("Unknown"),
    SPEED("Speed"),
    STAMINA("Stamina"),
    POWER("Power"),
    GUTS("Guts"),
    WIT("Wit"),
    FRIEND("Friend"),
    GROUP("Group");

    public final String displayName;

    SupportCardType(String displayName) {
        this.displayName = displayName;
    }
}
