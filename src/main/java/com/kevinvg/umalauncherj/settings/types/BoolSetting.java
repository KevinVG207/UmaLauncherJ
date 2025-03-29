package com.kevinvg.umalauncherj.settings.types;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kevinvg.umalauncherj.settings.Setting;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
public class BoolSetting extends Setting<Boolean> {
    public BoolSetting(Boolean value) {
        super(value, new TypeReference<>(){});
    }

    public BoolSetting(Boolean value, boolean hidden) {
        super(value, new TypeReference<>(){}, hidden);
    }

    public BoolSetting(Boolean value, boolean hidden, String tab) {
        super(value, new TypeReference<>(){}, hidden, tab);
    }
}
