package com.kevinvg.umalauncherj.settings;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.MINIMAL_CLASS
)
@NoArgsConstructor
@AllArgsConstructor
public abstract class Setting<T> {
    private String name = "";
    private String description = "";
    private boolean hidden;
    protected T value;
    private String tab = "General";

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
