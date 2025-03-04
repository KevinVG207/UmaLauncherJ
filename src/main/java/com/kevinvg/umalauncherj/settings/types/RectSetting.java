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
public class RectSetting extends Setting<Rect> {
    public RectSetting(Rect value, String name, String description) {
        super(value, Rect.class, name, description);
    }

    public RectSetting(Rect value, String name, String description, boolean hidden) {
        super(value, Rect.class, name, description, hidden);
    }

    public RectSetting(Rect value, String name, String description, boolean hidden, String tab) {
        super(value, Rect.class, name, description, hidden, tab);
    }
}
