package com.kevinvg.umalauncherj.helpertable.domain;

public enum Colors {
    ALERT("red"),
    WARNING("orange"),
    GOOD("lightgreen"),
    GREAT("aqua");

    public final String color;

    Colors(final String color) {
        this.color = color;
    }
}
