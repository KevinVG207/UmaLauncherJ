package com.kevinvg.umalauncherj.settings;

import com.kevinvg.umalauncherj.helpertable.Preset;
import com.kevinvg.umalauncherj.settings.types.ListSetting;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class AppSettings extends Settings<AppSettings.SettingKey> {
    public AppSettings() {
        this.settings.put(SettingKey.TRAINING_HELPER_TABLE_PRESET_LIST,
                new ListSetting<>(List.of(new Preset(), new Preset(), new Preset()), "Training helper table preset list", "List of presets for the automatic training event helper.", true));
    }

    enum SettingKey {
        TRAINING_HELPER_TABLE_PRESET_LIST
    }
}
