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
        this.settings.put(SettingKey.VERSION,
                new StringSetting(version != null ? version : "0.0.0", "Version", "Version", true));
        this.settings.put(SettingKey.SKIP_VERSION,
                new StringSetting("", "Skip update", "Version to skip updating to.", true));
        this.settings.put(SettingKey.ENABLE_LAUNCH_GAME,
                new BoolSetting(
                        true,
                        "Auto-start game.",
                        "Launch the game via DMM when Uma Launcher starts."
                ));
        this.settings.put(SettingKey.AUTOCLOSE_DMM,
                new BoolSetting(
                        true,
                        "Auto-close DMM",
                        "Close DMMGamePlayer automatically when the game is determined to be running."
                ));
        this.settings.put(SettingKey.LOCK_GAME_WINDOW,
                new BoolSetting(
                        true,
                        "Lock game window",
                        "Lock the game window to prevent accidental resizing.",
                        true
                ));
        this.settings.put(SettingKey.SELECTED_BROWSER,
                new ComboBoxSetting(
                        "Auto",
                        "Show support bonds",
                        "Choose how to display support bonds.",
                        List.of("Auto", "Firefox", "Chrome", "Edge")
                ));
        this.settings.put(SettingKey.WRITE_PACKETS,
                new BoolSetting(
                        false,
                        "Write packets",
                        "Write the latest packets to JSON.",
                        true
                ));
        this.settings.put(SettingKey.GAME_POSITION_PORTRAIT,
                new RectSetting(
                        new Rect(),
                        "Game position (portrait)",
                        "Game position (portrait)",
                        true
                ));
        this.settings.put(SettingKey.GAME_POSITION_LANDSCAPE,
                new RectSetting(
                        new Rect(),
                        "Game position (landscape)",
                        "Game position (landscape)",
                        true
                ));
        this.settings.put(SettingKey.BROWSER_POSITION,
                new RectSetting(
                        new Rect(),
                        "Browser position",
                        "Position of the browser window."
                ));
        this.settings.put(SettingKey.TRAINING_HELPER_TABLE_PRESET_LIST,
                new PresetListSetting(
                        List.of(new Preset()),
                        "Training helper table preset list",
                        "List of presets for the automatic training event helper.",
                        true
                ));
        this.settings.put(SettingKey.ENABLE_RICH_PRESENCE,
                new BoolSetting(
                        true,
                        "Enable Discord rich presence",
                        "Show current game status as an activity in Discord."
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
        this.settings.put(SettingKey.VPN_ENABLED,
                new BoolSetting(
                        false,
                        "Auto-VPN enabled",
                        "Connect to VPN when Uma Launcher is started.<br>For OpenVPN and SoftEther: A random JP server<br>will be chosen from VPN Gate to connect to.<br>NordVPN will connect to Japan."
                ));
        this.settings.put(SettingKey.VPN_DMM_ONLY,
                new BoolSetting(
                        true,
                        "VPN for DMM only",
                        "Disconnect from VPN after DMM Game Player is closed.<br>If unchecked, VPN will stay connected until Uma Launcher is closed."
                ));
        this.settings.put(SettingKey.VPN_CLIENT,
                new ComboBoxSetting(
                        "SoftEther",
                        "VPN client",
                        "Choose VPN client to use.<br>Restart Uma Launcher after changing this setting.",
                        List.of("NordVPN", "OpenVPN", "SoftEther")
                ));
        this.settings.put(SettingKey.VPN_OVERRIDE_STRING,
                new StringSetting(
                        "",
                        "VPN override (OpenVPN/SoftEther)",
                        "OpenVPN: Place a path to a custom ovpn profile.<br>SoftEther: Place an IP to override with port (Port is usually 443)"
                ));
        this.settings.put(SettingKey.VPN_CLIENT_PATH,
                new StringSetting(  // TODO: Make a FileDialog setting
                        "",
                        "VPN executable path (OpenVPN/NordVPN)",
                        "Path to the VPN client executable (openvpn.exe or nordvpn.exe).<br>Ignored for SoftEther."
                ));
        this.settings.put(SettingKey.MAXIMIZE_SAFEZONE,
                new SafezoneSetting(
                        new Safezone(0, 0, 0, 0),
                        "Safezone for \"Maximize + center game\" in tray menu",
                        "Amount of pixels to leave around the game window when maximizing.<br><b>If you are having issues streaming the game on Discord,</b> try adding a safezone of at least 8 pixels where your taskbar is."
                ));
    }
}
