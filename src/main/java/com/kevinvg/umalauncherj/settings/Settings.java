package com.kevinvg.umalauncherj.settings;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode
public abstract class Settings<T extends Enum<?>> {
    protected final Map<T, Setting<?>> settings = new HashMap<>();

    public Object getValue(final T key) {
        return settings.get(key).getValue();
    }

    public void setValue(final T key, final Object value) {
        settings.get(key).setValue(value);
    }
}
