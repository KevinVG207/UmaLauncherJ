package com.kevinvg.umalauncherj.settings.app;

import com.kevinvg.umalauncherj.helpertable.Preset;
import com.kevinvg.umalauncherj.settings.Settings;
import com.kevinvg.umalauncherj.settings.types.*;
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
        this.settings.put(SettingKey.SELECTED_BROWSER,
                new ComboBoxSetting(
                        "Auto",
                        "Show support bonds",
                        "Choose how to display support bonds.",
                        List.of("Auto", "Firefox", "Chrome", "Edge")
                ));
        this.settings.put(SettingKey.BROWSER_POSITION,
                new RectSetting(
                        null,
                        "Browser position",
                        "Position of the browser window."
                ));
        this.settings.put(SettingKey.TRAINING_HELPER_TABLE_PRESET_LIST,
                new ListSetting<>(
                        List.of(new Preset()),
                        "Training helper table preset list",
                        "List of presets for the automatic training event helper.",
                        true
                ));
        this.settings.put(SettingKey.GAMETORA_DARK_MODE,
                new BoolSetting(
                        true,
                        "GameTora dark mode",
                        "Enable dark mode for GameTora."
                ));
        this.settings.put(SettingKey.ENABLE_BROWSER_OVERRIDE,
                new BoolSetting(
                        false,
                        "Enable browser override",
                        "Enable overriding of the browser binary and driver. This also disables app mode for Chromium-based browsers, so you can reach settings in case things don't work."
                ));
        this.settings.put(SettingKey.BROWSER_CUSTOM_BINARY,
                new StringSetting(
                        "",
                        "Browser custom binary",
                        "Path to a custom browser executable.<br>Leave empty to let Selenium decide."
                ));
        this.settings.put(SettingKey.BROWSER_CUSTOM_DRIVER,
                new StringSetting(
                        "",
                        "Browser custom driver",
                        "Path to a custom browser driver.<br>Leave empty to let Selenium decide."
                ));
    }

    public enum SettingKey {
        VERSION,
        SELECTED_BROWSER,
        BROWSER_POSITION,
        TRAINING_HELPER_TABLE_PRESET_LIST,
        GAMETORA_DARK_MODE,
        ENABLE_BROWSER_OVERRIDE,
        BROWSER_CUSTOM_BINARY,
        BROWSER_CUSTOM_DRIVER
    }
}
