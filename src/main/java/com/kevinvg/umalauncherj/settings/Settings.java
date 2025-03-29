package com.kevinvg.umalauncherj.settings;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode
public abstract class Settings<T extends Enum<?>> {
    protected final Map<T, Setting<?>> settings = new HashMap<>();

    private final String locTitle;

    protected Settings(String locTitle) {
        this.locTitle = locTitle;
    }

    public Object getValue(T key) {
        return settings.get(key).getValue();
    }

    public void setValue(T key, Object value) {
        settings.get(key).setValue(value);
    }

    public String getLocName(T key) {
        return this.locTitle + "_NAME_" + key.name();
    }

    public String getLocDesc(T key) {
        return this.locTitle + "_DESC_" + key.name();
    }
}
