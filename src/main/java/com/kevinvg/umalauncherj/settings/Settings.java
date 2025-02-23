package com.kevinvg.umalauncherj.settings;

import com.kevinvg.umalauncherj.MapSerializable;

import java.util.HashMap;
import java.util.Map;

public abstract class Settings extends MapSerializable {
    protected final Map<String, Setting<?>> settings = new HashMap<>();

    @Override
    public Map<String, ?> toMap() {
        Map<String, Object> out = new HashMap<>();
        for (Map.Entry<String, Setting<?>> entry : settings.entrySet()) {
            String key = entry.getKey();
            Setting<?> setting = entry.getValue();
            out.put(key, setting.getValue());
        }
        return out;
    }

    @Override
    public void fromMap(Map<String, ?> map) {
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (settings.containsKey(key)) {
                settings.get(key).setValue(value);
            }
        }
    }
}
