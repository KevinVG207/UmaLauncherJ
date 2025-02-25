package com.kevinvg.umalauncherj.settings.types;

import com.kevinvg.umalauncherj.MapSerializable;
import com.kevinvg.umalauncherj.settings.MappableList;
import com.kevinvg.umalauncherj.settings.Setting;

import java.util.List;
import java.util.Map;

public class ListSetting<T extends MappableList<U>, U extends MapSerializable> extends Setting<T> {
    private final Class<U> clazz;

    public ListSetting(T value, Class<U> clazz, String name, String description) {
        super(value, name, description);
        this.clazz = clazz;
    }

    public ListSetting(T value, Class<U> clazz, String name, String description, boolean hidden) {
        super(value, name, description, hidden);
        this.clazz = clazz;
    }

    public ListSetting(T value, Class<U> clazz, String name, String description, boolean hidden, String tab) {
        super(value, name, description, hidden, tab);
        this.clazz = clazz;
    }

    public List<Map<String, ?>> toSerializedList() {
        return this.getValue().getList();
    }

    public boolean fromSerializedList(List<Map<String, ?>> list) {
        return this.setValue(new MappableList<>(list, clazz));
    }
}
