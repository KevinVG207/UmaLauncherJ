package com.kevinvg.umalauncherj.settings.types;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kevinvg.umalauncherj.settings.Setting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@Data
public class IntegerSetting extends Setting<Integer> {
    public IntegerSetting(Integer value, String name, String description) {
        super(value, new TypeReference<>(){}, name, description);
    }

    public IntegerSetting(Integer value, String name, String description, boolean hidden) {
        super(value, new TypeReference<>(){}, name, description, hidden);
    }

    public IntegerSetting(Integer value, String name, String description, boolean hidden, String tab) {
        super(value, new TypeReference<>(){}, name, description, hidden, tab);
    }
}
