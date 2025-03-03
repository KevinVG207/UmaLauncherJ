package com.kevinvg.umalauncherj.settings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
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
    @JsonIgnore
    private String name = "";
    @JsonIgnore
    private String description = "";
    protected T value;
    @JsonIgnore
    private String tab = "General";
    @JsonIgnore
    private boolean hidden;

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

    @JsonIgnore
    public Object getValue2() {
        return value;
    }

}
