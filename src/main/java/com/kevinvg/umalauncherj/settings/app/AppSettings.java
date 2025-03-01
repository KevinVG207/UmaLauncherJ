package com.kevinvg.umalauncherj.settings.app;

import com.kevinvg.umalauncherj.helpertable.Preset;
import com.kevinvg.umalauncherj.settings.Settings;
import com.kevinvg.umalauncherj.settings.types.ListSetting;
import com.kevinvg.umalauncherj.settings.types.StringSetting;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;

public class AppSettings extends Settings<AppSettings.SettingKey> {
    @ConfigProperty(name = "quarkus.application.version")
    protected String version;

    public AppSettings() {
        this.settings.put(SettingKey.VERSION,
                new StringSetting(version, "Version", "Version", true));
        this.settings.put(SettingKey.TRAINING_HELPER_TABLE_PRESET_LIST,
                new ListSetting<>(List.of(new Preset(), new Preset(), new Preset()), "Training helper table preset list", "List of presets for the automatic training event helper.", true));
    }

    enum SettingKey {
        VERSION,
        TRAINING_HELPER_TABLE_PRESET_LIST
    }
}
