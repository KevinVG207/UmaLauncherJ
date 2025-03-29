package com.kevinvg.umalauncherj.settings.app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kevinvg.umalauncherj.helpertable.Preset;
import com.kevinvg.umalauncherj.settings.Settings;
import com.kevinvg.umalauncherj.settings.types.*;
import com.kevinvg.umalauncherj.util.Rect;
import com.kevinvg.umalauncherj.util.Safezone;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.ConfigProvider;

import java.util.List;

@Slf4j
@EqualsAndHashCode(callSuper = true)
public class AppSettings extends Settings<AppSettings.SettingKey> {
    public enum SettingKey {
        VERSION,
        SKIP_VERSION,
        ENABLE_BETA,
        WRITE_PACKETS,
        LOCK_GAME_WINDOW,
        AUTOCLOSE_DMM,
        SELECTED_BROWSER,
        BROWSER_POSITION,
        ENABLE_RICH_PRESENCE,
        ENABLE_LAUNCH_GAME,
        TRAINING_HELPER_TABLE_PRESET_LIST,
        GAMETORA_DARK_MODE,
        ENABLE_BROWSER_OVERRIDE,
        BROWSER_CUSTOM_BINARY,
        BROWSER_CUSTOM_DRIVER,
        VPN_ENABLED,
        VPN_DMM_ONLY,
        VPN_CLIENT,
        VPN_OVERRIDE_STRING,
        VPN_CLIENT_PATH,
        GAME_POSITION_PORTRAIT,
        GAME_POSITION_LANDSCAPE,
        MAXIMIZE_SAFEZONE
    }

    protected String version = ConfigProvider.getConfig().getValue("quarkus.application.version", String.class);

    public AppSettings() {
        super("APPSETTINGS");
        this.settings.put(SettingKey.VERSION,
                new StringSetting(version != null ? version : "0.0.0", true));
        this.settings.put(SettingKey.SKIP_VERSION,
                new StringSetting("", true));
        this.settings.put(SettingKey.ENABLE_BETA,
                new BoolSetting(
                        false
                ));
        this.settings.put(SettingKey.ENABLE_LAUNCH_GAME,
                new BoolSetting(
                        true
                ));
        this.settings.put(SettingKey.AUTOCLOSE_DMM,
                new BoolSetting(
                        true
                ));
        this.settings.put(SettingKey.LOCK_GAME_WINDOW,
                new BoolSetting(
                        true,
                        true
                ));
        this.settings.put(SettingKey.SELECTED_BROWSER,
                new ComboBoxSetting(
                        "AUTO",
                        List.of("AUTO", "FIREFOX", "CHROME", "EDGE")
                ));
        this.settings.put(SettingKey.WRITE_PACKETS,
                new BoolSetting(
                        false,
                        true
                ));
        this.settings.put(SettingKey.GAME_POSITION_PORTRAIT,
                new RectSetting(
                        new Rect(),
                        true
                ));
        this.settings.put(SettingKey.GAME_POSITION_LANDSCAPE,
                new RectSetting(
                        new Rect(),
                        true
                ));
        this.settings.put(SettingKey.BROWSER_POSITION,
                new RectSetting(
                        new Rect()
                ));
        this.settings.put(SettingKey.TRAINING_HELPER_TABLE_PRESET_LIST,
                new PresetListSetting(
                        List.of(new Preset()),
                        true
                ));
        this.settings.put(SettingKey.ENABLE_RICH_PRESENCE,
                new BoolSetting(
                        true
                ));
        this.settings.put(SettingKey.GAMETORA_DARK_MODE,
                new BoolSetting(
                        true
                ));
        this.settings.put(SettingKey.ENABLE_BROWSER_OVERRIDE,
                new BoolSetting(
                        false
                ));
        this.settings.put(SettingKey.BROWSER_CUSTOM_BINARY,
                new StringSetting(
                        ""
                ));
        this.settings.put(SettingKey.BROWSER_CUSTOM_DRIVER,
                new StringSetting(
                        ""
                ));
        this.settings.put(SettingKey.VPN_ENABLED,
                new BoolSetting(
                        false
                ));
        this.settings.put(SettingKey.VPN_DMM_ONLY,
                new BoolSetting(
                        true
                ));
        this.settings.put(SettingKey.VPN_CLIENT,
                new ComboBoxSetting(
                        "SOFTETHER",
                        List.of("NORDVPN", "OPENVPN", "SOFTETHER")
                ));
        this.settings.put(SettingKey.VPN_OVERRIDE_STRING,
                new StringSetting(
                        ""
                ));
        this.settings.put(SettingKey.VPN_CLIENT_PATH,
                new StringSetting(  // TODO: Make a FileDialog setting
                        ""
                ));
        this.settings.put(SettingKey.MAXIMIZE_SAFEZONE,
                new SafezoneSetting(
                        new Safezone(0, 0, 0, 0)
                ));
    }
}
