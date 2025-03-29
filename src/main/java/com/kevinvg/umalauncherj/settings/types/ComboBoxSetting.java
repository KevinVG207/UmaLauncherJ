package com.kevinvg.umalauncherj.settings.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.kevinvg.umalauncherj.settings.Setting;
import lombok.*;

import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ComboBoxSetting extends Setting<String> {
    @JsonIgnore
    private List<String> choices = Collections.emptyList();

    public ComboBoxSetting(String value, List<String> choices) {
        this(value, false, choices);
    }

    public ComboBoxSetting(String value, boolean hidden, List<String> choices) {
        super(null, new TypeReference<>(){}, hidden);
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
