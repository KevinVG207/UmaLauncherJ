package com.kevinvg.umalauncherj.helpertable.domain;

public enum CommandType implements Comparable<CommandType> {
    UNKNOWN("unknown", -1),
    SPEED("speed", 0),
    STAMINA("stamina", 1),
    POWER("power", 2),
    GUTS("guts", 3),
    WIT("wiz", 4),
    SS_MATCH("ss_match", 5);

    public final String internalName;
    public final int order;

    CommandType(String internalName, int order) {
        this.internalName = internalName;
        this.order = order;
    }
}