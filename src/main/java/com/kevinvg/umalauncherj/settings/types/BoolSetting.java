package com.kevinvg.umalauncherj.settings.types;

import com.kevinvg.umalauncherj.settings.Setting;

public class BoolSetting extends Setting<Boolean> {
    public BoolSetting(Boolean value, String name, String description) {
        super(value, name, description);
    }

    public BoolSetting(Boolean value, String name, String description, boolean hidden) {
        super(value, name, description, hidden);
    }

    public BoolSetting(Boolean value, String name, String description, boolean hidden, String tab) {
        super(value, name, description, hidden, tab);
    }
}
