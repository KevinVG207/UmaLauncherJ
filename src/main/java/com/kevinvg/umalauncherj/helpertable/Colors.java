package com.kevinvg.umalauncherj.helpertable;

public enum Colors {
    ALERT("red"),
    WARNING("orange"),
    GOOD("lightgreen"),
    GREAT("aqua");

    public final String cssString;

    Colors(final String cssString) {
        this.cssString = cssString;
    }
}
