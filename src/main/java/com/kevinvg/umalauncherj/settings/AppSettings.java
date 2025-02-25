package com.kevinvg.umalauncherj.settings;

import com.kevinvg.umalauncherj.helpertable.Preset;
import com.kevinvg.umalauncherj.settings.types.ListSetting;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class AppSettings extends Settings{
    public AppSettings() {
        this.settings.put("training_helper_table_preset_list",
                new ListSetting<>(new MappableList<>(Preset.class, List.of(new Preset(), new Preset(), new Preset())), Preset.class, "Training helper table preset list", "List of presets for the automatic training event helper.", true));
    }
}
