package com.kevinvg.umalauncherj.helpertable;

import com.kevinvg.umalauncherj.settings.types.BoolSetting;
import com.kevinvg.umalauncherj.settings.types.ComboBoxSetting;
import com.kevinvg.umalauncherj.settings.Settings;

import java.util.List;

public class PresetSettings extends Settings<PresetSettings.SettingKey> {
    public PresetSettings() {
        super("PRESETSETTINGS");
        this.settings.put(SettingKey.PROGRESS_BAR,
                new BoolSetting(
                        true
                ));
        this.settings.put(SettingKey.ENERGY_ENABLED,
                new BoolSetting(
                        true
                ));
        this.settings.put(SettingKey.SUPPORT_BONDS,
                new ComboBoxSetting(
                        "Bar",
                        List.of("OFF", "NUMBER", "BAR", "BOTH")
                ));
        this.settings.put(SettingKey.HIDE_SUPPORT_BONDS,
                new BoolSetting(
                        true
                ));
        this.settings.put(SettingKey.DISPLAYED_VALUE,
                new ComboBoxSetting(
                        "Raw gained stats",
                        List.of("RAW", "COMPENSATED", "BOTH")
                ));
        this.settings.put(SettingKey.SKILLPT_ENABLED,
                new BoolSetting(
                        false
                ));
        this.settings.put(SettingKey.FANS_ENABLED,
                new BoolSetting(
                        false
                ));
        this.settings.put(SettingKey.SCHEDULE_ENABLED,
                new BoolSetting(
                        true
                ));
        this.settings.put(SettingKey.SCENARIO_SPECIFIC_ENABLED,
                new BoolSetting(
                        true
                ));
    }

    enum SettingKey {
        PROGRESS_BAR,
        ENERGY_ENABLED,
        SUPPORT_BONDS,
        HIDE_SUPPORT_BONDS,
        DISPLAYED_VALUE,
        SKILLPT_ENABLED,
        FANS_ENABLED,
        SCHEDULE_ENABLED,
        SCENARIO_SPECIFIC_ENABLED
    }
}
