package com.kevinvg.umalauncherj.settings;

import com.kevinvg.umalauncherj.MapSerializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MappableList<T extends MapSerializable> {
    private final List<T> list;

    public MappableList(Class<T> clazz) {
        this.list = new ArrayList<>();
    }

    public MappableList(Class<T> clazz, List<T> list) {
        this.list = Collections.unmodifiableList(list);
    }

    public MappableList(List<Map<String,?>> listWithMaps, Class<T> clazz) {
        List<T> out = new ArrayList<>(listWithMaps.size());
        for (Map<String,?> map : listWithMaps) {
            T instance;
            try {
                instance = clazz.getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            instance.fromMap(map);
            out.add(instance);
        }
        this.list = out;
    }

    public List<Map<String,?>> getList() {
        List<Map<String,?>> out = new ArrayList<>(list.size());
        for (T item : list) {
            out.add(item.toMap());
        }
        return out;
    }
}
