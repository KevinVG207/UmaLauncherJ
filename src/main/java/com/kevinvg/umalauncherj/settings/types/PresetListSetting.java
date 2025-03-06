package com.kevinvg.umalauncherj.settings.types;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kevinvg.umalauncherj.helpertable.Preset;
import com.kevinvg.umalauncherj.settings.Setting;

import java.util.List;

public class PresetListSetting extends Setting<List<Preset>> {
    public PresetListSetting(List<Preset> value, String name, String description) {
        super(value, new TypeReference<>(){}, name, description);
    }

    public PresetListSetting(List<Preset> value, String name, String description, boolean hidden) {
        super(value, new TypeReference<>(){}, name, description, hidden);
    }

    public PresetListSetting(List<Preset> value, String name, String description, boolean hidden, String tab) {
        super(value, new TypeReference<>(){}, name, description, hidden, tab);
    }
}
