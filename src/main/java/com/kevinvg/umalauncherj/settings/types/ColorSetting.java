package com.kevinvg.umalauncherj.settings.types;

public class ColorSetting extends StringSetting {
    public ColorSetting(String value, String name, String description) {
        super(value, name, description);
    }

    public ColorSetting(String value, String name, String description, boolean hidden) {
        super(value, name, description, hidden);
    }

    public ColorSetting(String value, String name, String description, boolean hidden, String tab) {
        super(value, name, description, hidden, tab);
    }
}
