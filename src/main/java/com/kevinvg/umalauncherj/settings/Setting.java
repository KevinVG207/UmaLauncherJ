package com.kevinvg.umalauncherj.settings;

public abstract class Setting<T> {
    private final String name;
    private final String description;
    private boolean hidden;
    private T value;
    private String tab = "General";

    Setting(T value, String name, String description, boolean hidden) {
        this.value = value;
        this.name = name;
        this.description = description;
        this.hidden = hidden;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
