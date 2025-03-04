package com.kevinvg.umalauncherj.settings;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import lombok.Data;
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
    protected String value;
    @JsonIgnore
    private String tab = "General";
    @JsonIgnore
    private boolean hidden;

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

    public boolean setValue(Object value) {
        if (value instanceof String str) {
            this.value = str;
            return true;
        }

        try {
            this.value = mapper.writeValueAsString(value);
            return true;
        } catch (JsonProcessingException e) {
            log.error("Error while setting setting", e);
        }
        return false;
    }

    public T getValue() {
        try {
            String value = this.value;
            if (value == null) return null;
            if (value.equals("null")) return null;
            if (clazz == String.class) {
                return (T) value;
            }
            // FIXME: https://medium.com/@poojaauma/enhancing-deserialization-safety-in-java-with-typereference-ea110ece6994
            return mapper.readValue(value, clazz);
        } catch (JsonProcessingException e) {
            log.error("Error while getting rect", e);
            return null;
        }
    }
}
