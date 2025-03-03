package com.kevinvg.umalauncherj.settings.types;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevinvg.umalauncherj.util.Rect;
import com.kevinvg.umalauncherj.settings.Setting;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RectSetting extends Setting<String> {
    public static final ObjectMapper mapper = new ObjectMapper();

    public RectSetting(String value, String name, String description) {
        super(value, name, description);
    }

    public RectSetting(String value, String name, String description, boolean hidden) {
        super(value, name, description, hidden);
    }

    public RectSetting(String value, String name, String description, boolean hidden, String tab) {
        super(value, name, description, hidden, tab);
    }

    @Override
    public boolean setValue(Object value) {
        if (value instanceof Rect rect) {
            try {
                _setValue(mapper.writeValueAsString(rect));
                return true;
            } catch (JsonProcessingException e) {
                log.error("Error while setting rect", e);
            }
        } else if (value instanceof String str) {
            _setValue(str);
        }
        return false;
    }

    @Override
    public Rect getValue2() {
        try {
            String value = super.getValue();
            if (value == null) return null;
            return mapper.readValue(value, Rect.class);
        } catch (JsonProcessingException e) {
            log.error("Error while getting rect", e);
            return null;
        }
    }
}
