package com.kevinvg.umalauncherj.settings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.MINIMAL_CLASS
)
@NoArgsConstructor
@AllArgsConstructor
public abstract class Setting<T> {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(
                SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true
        );
    }

    @JsonIgnore
    private String name = "";
    @JsonIgnore
    private String description = "";
    @Getter
    protected T value;
    @JsonIgnore
    private String tab = "General";
    @JsonIgnore
    private boolean hidden;

    @JsonIgnore
    private Class<T> clazz;

    protected Setting(T value, Class<T> clazz, String name, String description) {
        this(value, clazz, name, description, false);
    }

    protected Setting(T value, Class<T> clazz, String name, String description, boolean hidden) {
        this(value, clazz, name, description, hidden, "General");
    }

    protected Setting(T value, Class<T> clazz, String name, String description, boolean hidden, String tab) {
        this.clazz = clazz;
        this.name = name;
        this.description = description;
        this.hidden = hidden;
        this.tab = tab;
        setValue(value);
    }

    @SuppressWarnings("unchecked")
    public boolean setValue(Object value) {
        try {
            this.value = (T) value;
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

}
