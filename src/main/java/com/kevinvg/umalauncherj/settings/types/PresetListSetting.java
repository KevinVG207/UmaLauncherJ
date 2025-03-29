package com.kevinvg.umalauncherj.settings.types;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kevinvg.umalauncherj.helpertable.Preset;
import com.kevinvg.umalauncherj.settings.Setting;

import java.util.List;

public class PresetListSetting extends Setting<List<Preset>> {
    public PresetListSetting(List<Preset> value, String name, String description) {
        super(value, new TypeReference<>(){});
    }

    public PresetListSetting(List<Preset> value, boolean hidden) {
        super(value, new TypeReference<>(){}, hidden);
    }

    public PresetListSetting(List<Preset> value, boolean hidden, String tab) {
        super(value, new TypeReference<>(){}, hidden, tab);
    }
}
