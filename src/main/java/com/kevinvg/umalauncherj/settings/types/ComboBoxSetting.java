package com.kevinvg.umalauncherj.settings.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kevinvg.umalauncherj.settings.Setting;
import lombok.*;

import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ComboBoxSetting extends Setting<String> {
    @JsonIgnore
    private List<String> choices = Collections.emptyList();

    public ComboBoxSetting(String value, String name, String description, List<String> choices) {
        this(value, name, description, false, choices);
    }

    public ComboBoxSetting(String value, String name, String description, boolean hidden, List<String> choices) {
        super(null, String.class, name, description, hidden);
        this.choices = Collections.unmodifiableList(choices);
        setValue(value);
    }

    @Override
    public boolean setValue(Object value) {
        if (!(value instanceof String stringValue)) return false;

        int idx = choices.indexOf(stringValue);
        if (idx == -1) {
            return false;
        }
        this.value = stringValue;
        return true;
    }

    @Override
    public String getValue() {
        if (!choices.isEmpty() && !choices.contains(value)) {
            return choices.getFirst();
        }

        return value;
    }
}
