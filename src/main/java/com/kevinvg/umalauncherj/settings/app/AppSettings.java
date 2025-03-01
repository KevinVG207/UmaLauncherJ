package com.kevinvg.umalauncherj.settings.app;

import com.kevinvg.umalauncherj.helpertable.Preset;
import com.kevinvg.umalauncherj.settings.Settings;
import com.kevinvg.umalauncherj.settings.types.ListSetting;
import com.kevinvg.umalauncherj.settings.types.StringSetting;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.ConfigProvider;

import java.util.List;

@Slf4j
@EqualsAndHashCode(callSuper = true)
public class AppSettings extends Settings<AppSettings.SettingKey> {
    protected String version = ConfigProvider.getConfig().getValue("quarkus.application.version", String.class);

    public AppSettings() {
        this.settings.put(SettingKey.VERSION,
                new StringSetting(version != null ? version : "0.0.0", "Version", "Version", true));
        this.settings.put(SettingKey.TRAINING_HELPER_TABLE_PRESET_LIST,
                new ListSetting<>(List.of(new Preset()), "Training helper table preset list", "List of presets for the automatic training event helper.", true));
    }

    enum SettingKey {
        VERSION,
        TRAINING_HELPER_TABLE_PRESET_LIST
    }
}
