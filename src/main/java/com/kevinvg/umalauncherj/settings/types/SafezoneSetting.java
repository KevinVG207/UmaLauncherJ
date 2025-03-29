package com.kevinvg.umalauncherj.settings.types;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kevinvg.umalauncherj.settings.Setting;
import com.kevinvg.umalauncherj.util.Safezone;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SafezoneSetting extends Setting<Safezone> {
    public SafezoneSetting(Safezone value) {
        super(value, new TypeReference<>(){});
    }

    public SafezoneSetting(Safezone value, boolean hidden) {
        super(value, new TypeReference<>(){}, hidden);
    }

    public SafezoneSetting(Safezone value, boolean hidden, String tab) {
        super(value, new TypeReference<>(){}, hidden, tab);
    }
}