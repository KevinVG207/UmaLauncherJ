package com.kevinvg.umalauncherj.settings.types;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kevinvg.umalauncherj.settings.Setting;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
public class BoolSetting extends Setting<Boolean> {
    public BoolSetting(Boolean value, String name, String description) {
        super(value, new TypeReference<>(){}, name, description);
    }

    public BoolSetting(Boolean value, String name, String description, boolean hidden) {
        super(value, new TypeReference<>(){}, name, description, hidden);
    }

    public BoolSetting(Boolean value, String name, String description, boolean hidden, String tab) {
        super(value, new TypeReference<>(){}, name, description, hidden, tab);
    }
}
