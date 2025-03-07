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
    public SafezoneSetting(Safezone value, String name, String description) {
        super(value, new TypeReference<>(){}, name, description);
    }

    public SafezoneSetting(Safezone value, String name, String description, boolean hidden) {
        super(value, new TypeReference<>(){}, name, description, hidden);
    }

    public SafezoneSetting(Safezone value, String name, String description, boolean hidden, String tab) {
        super(value, new TypeReference<>(){}, name, description, hidden, tab);
    }
}