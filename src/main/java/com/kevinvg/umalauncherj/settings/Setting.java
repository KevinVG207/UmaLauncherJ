package com.kevinvg.umalauncherj.settings;

import lombok.Getter;

public abstract class Setting<T> {
    private final String name;
    private final String description;
    private boolean hidden;
    @Getter
    protected T value;
    private final String tab;

    protected Setting(T value, String name, String description) {
        this(value, name, description, false);
    }

    protected Setting(T value, String name, String description, boolean hidden) {
        this(value, name, description, hidden, "General");
    }

    protected Setting(T value, String name, String description, boolean hidden, String tab) {
        this.value = value;
        this.name = name;
        this.description = description;
        this.hidden = hidden;
        this.tab = tab;
    }

    @SuppressWarnings("unchecked")
    public boolean setValue(Object value) {
        T castValue;
        try {
            castValue = (T) value;
        } catch (ClassCastException e) {
            return false;
        }
        return _setValue(castValue);
    }

    protected boolean _setValue(T value) {
        this.value = value;
        return true;
    }

}
