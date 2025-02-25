package com.kevinvg.umalauncherj.settings;

import com.kevinvg.umalauncherj.MapSerializable;
import com.kevinvg.umalauncherj.settings.types.ListSetting;
import com.kevinvg.umalauncherj.settings.types.MappableSetting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Settings implements MapSerializable {
    protected final Map<String, Setting<?>> settings = new HashMap<>();

    @Override
    public Map<String, ?> toMap() {
        Map<String, Object> out = new HashMap<>();
        for (Map.Entry<String, Setting<?>> entry : settings.entrySet()) {
            String key = entry.getKey();
            Setting<?> setting = entry.getValue();
            if (setting instanceof MappableSetting<?> mappableSetting) {
                out.put(key, mappableSetting.getValue().toMap());
            } else if (setting instanceof MapSerializable mapSerializable) {
                out.put(key, mapSerializable.toMap());
            } else if (setting instanceof ListSetting listSetting) {
                out.put(key, listSetting.toSerializedList());
            } else {
                out.put(key, setting.getValue());
            }
        }
        return out;
    }

    @Override
//    @SuppressWarnings("unchecked")
    public void fromMap(Map<String, ?> map) {
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (settings.containsKey(key)) {
                var setting = settings.get(key);
                try{
                    if (setting instanceof MappableSetting<?> mappableSetting) {
                        mappableSetting.setValueFromMap(value);
                    } else if (setting instanceof MapSerializable mapSerializable) {
                        mapSerializable.fromMap((Map<String, ?>) value);
                    } else if(setting instanceof ListSetting listSetting) {
                        listSetting.fromSerializedList((List<Map<String, ?>>) value);
                    } else {
                        settings.get(key).setValue(value);
                    }
                } catch (ClassCastException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
}
