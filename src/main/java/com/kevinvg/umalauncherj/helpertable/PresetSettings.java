package com.kevinvg.umalauncherj.helpertable;

import com.kevinvg.umalauncherj.settings.types.BoolSetting;
import com.kevinvg.umalauncherj.settings.types.ComboBoxSetting;
import com.kevinvg.umalauncherj.settings.Settings;

import java.util.List;

public class PresetSettings extends Settings<PresetSettings.SettingKey> {
    public PresetSettings() {
        this.settings.put(SettingKey.PROGRESS_BAR,
                new BoolSetting(
                        true,
                        "Show progress bar",
                        "Displays the training run progress."
                ));
        this.settings.put(SettingKey.ENERGY_ENABLED,
                new BoolSetting(
                        true,
                        "Show energy",
                        "Displays energy in the event helper."
                ));
        this.settings.put(SettingKey.SUPPORT_BONDS,
                new ComboBoxSetting(
                        "Bar",
                        "Show support bonds",
                        "Choose how to display support bonds.",
                        List.of("Off", "Number", "Bar", "Both")
                ));
        this.settings.put(SettingKey.HIDE_SUPPORT_BONDS,
                new BoolSetting(
                        true,
                        "Auto-hide maxed supports",
                        "When support bonds are enabled, automatically hide characters when they reach 100."
                ));
        this.settings.put(SettingKey.DISPLAYED_VALUE,
                new ComboBoxSetting(
                        "Raw gained stats",
                        "Displayed value(s) for stats",
                        "Which value(s) to display for stats rows.",
                        List.of("Raw gained stats", "Overcap-compensated gained stats", "Both")
                ));
        this.settings.put(SettingKey.SKILLPT_ENABLED,
                new BoolSetting(
                        false,
                        "Show skill points",
                        "Displays skill points in the event helper."
                ));
        this.settings.put(SettingKey.FANS_ENABLED,
                new BoolSetting(
                        false,
                        "Show fans",
                        "Displays fans in the event helper."
                ));
        this.settings.put(SettingKey.SCHEDULE_ENABLED,
                new BoolSetting(
                        true,
                        "Show schedule countdown",
                        "Displays the amount of turns until your next scheduled race. (If there is one.)"
                ));
        this.settings.put(SettingKey.SCENARIO_SPECIFIC_ENABLED,
                new BoolSetting(
                        true,
                        "Show scenario specific elements",
                        "Show scenario specific elements in the event helper, above the main table."
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
