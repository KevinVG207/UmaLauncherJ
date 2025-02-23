package com.kevinvg.umalauncherj.settings;

import java.util.Collections;
import java.util.List;

public class ComboBoxSetting extends Setting<String> {
    private List<String> choices;

    private int value;

    public ComboBoxSetting(String value, String name, String description, List<String> choices) {
        this(value, name, description, false, choices);
    }

    public ComboBoxSetting(String value, String name, String description, boolean hidden, List<String> choices) {
        super(value, name, description, hidden);
        this.choices = Collections.unmodifiableList(choices);
    }

    @Override
    public String getValue() {
        return choices.get(value);
    }

    @Override
    public void setValue(String value) {
        this.value = choices.indexOf(value);
    }
}
