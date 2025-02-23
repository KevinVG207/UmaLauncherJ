package com.kevinvg.umalauncherj.gametora;

public enum GtLanguage {
    ENGLISH(""),
    JAPANESE("/ja"),
    KOREAN("/ko"),
    TAIWANESE("/zh-tw");

    public final String urlSegment;

    GtLanguage(final String urlSegment) {
        this.urlSegment = urlSegment;
    }
}
