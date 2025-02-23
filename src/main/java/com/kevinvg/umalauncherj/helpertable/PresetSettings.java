package com.kevinvg.umalauncherj.helpertable;

import com.kevinvg.umalauncherj.settings.BoolSetting;
import com.kevinvg.umalauncherj.settings.ComboBoxSetting;
import com.kevinvg.umalauncherj.settings.Setting;
import com.kevinvg.umalauncherj.settings.Settings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PresetSettings extends Settings {
    private final Map<String, Setting<?>> settings = new HashMap<>();

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
    }

}
