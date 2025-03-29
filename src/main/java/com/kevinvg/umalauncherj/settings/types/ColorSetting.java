package com.kevinvg.umalauncherj.settings.types;

public class ColorSetting extends StringSetting {
    public ColorSetting(String value) {
        super(value);
    }

    public ColorSetting(String value, boolean hidden) {
        super(value, hidden);
    }

    public ColorSetting(String value, boolean hidden, String tab) {
        super(value, hidden, tab);
    }
}
