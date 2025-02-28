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
public class StringSetting extends Setting<String> {
    public StringSetting(String value, String name, String description) {
        super(value, name, description);
    }

    public StringSetting(String value, String name, String description, boolean hidden) {
        super(value, name, description, hidden);
    }

    public StringSetting(String value, String name, String description, boolean hidden, String tab) {
        super(value, name, description, hidden, tab);
    }
}
