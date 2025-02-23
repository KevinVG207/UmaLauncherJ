package com.kevinvg.umalauncherj.helpertable;

import com.kevinvg.umalauncherj.settings.types.BoolSetting;
import com.kevinvg.umalauncherj.settings.types.ComboBoxSetting;
import com.kevinvg.umalauncherj.settings.Settings;

import java.util.List;

public class PresetSettings extends Settings {
    public PresetSettings() {
        this.settings.put("progress_bar",
                new BoolSetting(
                        true,
                        "Show progress bar",
                        "Displays the training run progress."
                ));
        this.settings.put("energy_enabled",
                new BoolSetting(
                        true,
                        "Show energy",
                        "Displays energy in the event helper."
                ));
        this.settings.put("support_bonds",
                new ComboBoxSetting(
                        "Bar",
                        "Show support bonds",
                        "Choose how to display support bonds.",
                        List.of("Off", "Number", "Bar", "Both")
                ));
        this.settings.put("hide_support_bonds",
                new BoolSetting(
                        true,
                        "Auto-hide maxed supports",
                        "When support bonds are enabled, automatically hide characters when they reach 100."
                ));
        this.settings.put("displayed_value",
                new ComboBoxSetting(
                        "Raw gained stats",
                        "Displayed value(s) for stats",
                        "Which value(s) to display for stats rows.",
                        List.of("Raw gained stats", "Overcap-compensated gained stats", "Both")
                ));
        this.settings.put("skillpt_enabled",
                new BoolSetting(
                        false,
                        "Show skill points",
                        "Displays skill points in the event helper."
                ));
        this.settings.put("fans_enabled",
                new BoolSetting(
                        false,
                        "Show fans",
                        "Displays fans in the event helper."
                ));
        this.settings.put("schedule_enabled",
                new BoolSetting(
                        true,
                        "Show schedule countdown",
                        "Displays the amount of turns until your next scheduled race. (If there is one.)"
                ));
        this.settings.put("scenario_specific_enabled",
                new BoolSetting(
                        true,
                        "Show scenario specific elements",
                        "Show scenario specific elements in the event helper, above the main table."
                ));
    }
}
