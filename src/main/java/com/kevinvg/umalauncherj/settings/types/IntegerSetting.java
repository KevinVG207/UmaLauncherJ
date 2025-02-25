package com.kevinvg.umalauncherj.settings.types;

import com.kevinvg.umalauncherj.settings.Setting;

public class IntegerSetting extends Setting<Integer> {
    public IntegerSetting(Integer value, String name, String description) {
        super(value, name, description);
    }

    public IntegerSetting(Integer value, String name, String description, boolean hidden) {
        super(value, name, description, hidden);
    }

    public IntegerSetting(Integer value, String name, String description, boolean hidden, String tab) {
        super(value, name, description, hidden, tab);
    }
}
