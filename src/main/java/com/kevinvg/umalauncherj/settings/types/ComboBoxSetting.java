package com.kevinvg.umalauncherj.settings.types;

import com.kevinvg.umalauncherj.settings.Setting;
import lombok.*;

import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ComboBoxSetting extends Setting<String> {
    private List<String> choices = Collections.emptyList();

    public ComboBoxSetting(String value, String name, String description, List<String> choices) {
        this(value, name, description, false, choices);
    }

    public ComboBoxSetting(String value, String name, String description, boolean hidden, List<String> choices) {
        super(value, name, description, hidden);
        this.choices = Collections.unmodifiableList(choices);
    }

    @Override
    protected boolean _setValue(String value) {
        int idx = choices.indexOf(value);
        if (idx != -1) {
            return false;
        }
        this.value = value;
        return true;
    }
}
