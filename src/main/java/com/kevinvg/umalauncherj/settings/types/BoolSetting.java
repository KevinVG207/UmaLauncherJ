package com.kevinvg.umalauncherj.settings.types;

import com.kevinvg.umalauncherj.settings.Setting;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
public class BoolSetting extends Setting<Boolean> {
    public BoolSetting(Boolean value, String name, String description) {
        super(value, Boolean.class, name, description);
    }

    public BoolSetting(Boolean value, String name, String description, boolean hidden) {
        super(value, Boolean.class, name, description, hidden);
    }

    public BoolSetting(Boolean value, String name, String description, boolean hidden, String tab) {
        super(value, Boolean.class, name, description, hidden, tab);
    }
}
