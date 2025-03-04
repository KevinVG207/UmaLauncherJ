package com.kevinvg.umalauncherj.settings.types;

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
        super(value, Integer.class, name, description);
    }

    public IntegerSetting(Integer value, String name, String description, boolean hidden) {
        super(value, Integer.class, name, description, hidden);
    }

    public IntegerSetting(Integer value, String name, String description, boolean hidden, String tab) {
        super(value, Integer.class, name, description, hidden, tab);
    }
}
