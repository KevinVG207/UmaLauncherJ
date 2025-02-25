package com.kevinvg.umalauncherj.settings.types;

import com.kevinvg.umalauncherj.MapSerializable;
import com.kevinvg.umalauncherj.settings.Setting;

import java.util.Map;

public abstract class MappableSetting<T extends MapSerializable> extends Setting<T> {
    private final Class<T> clazz;

    protected MappableSetting(T value, Class<T> clazz, String name, String description) {
        super(value, name, description);
        this.clazz = clazz;
    }

    protected MappableSetting(T value, Class<T> clazz, String name, String description, boolean hidden) {
        super(value, name, description, hidden);
        this.clazz = clazz;
    }

    protected MappableSetting(T value, Class<T> clazz, String name, String description, boolean hidden, String tab) {
        super(value, name, description, hidden, tab);
        this.clazz = clazz;
    }

    @SuppressWarnings("unchecked")
    public boolean setValueFromMap(Object map) {
        Map<String, ?> castMap;
        try {
            castMap = (Map<String, ?>) map;
        } catch (ClassCastException e) {
            return false;
        }

        T instance;
        try {
            instance = this.clazz.getConstructor().newInstance();
        } catch (Exception e) {
            return false;
        }
        instance.fromMap(castMap);
        super.setValue(instance);
        return true;
    }

}
