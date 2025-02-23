package com.kevinvg.umalauncherj.settings;

public class BoolSetting extends Setting<Boolean>{
    public BoolSetting(Boolean value, String name, String description) {
        this(value, name, description, false);
    }

    public BoolSetting(Boolean value, String name, String description, boolean hidden) {
        super(value, name, description, hidden);
    }
}
