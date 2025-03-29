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
public class StringSetting extends Setting<String> {
    public StringSetting(String value) {
        super(value, new TypeReference<>(){});
    }

    public StringSetting(String value, boolean hidden) {
        super(value, new TypeReference<>(){}, hidden);
    }

    public StringSetting(String value, boolean hidden, String tab) {
        super(value, new TypeReference<>(){}, hidden, tab);
    }
}
