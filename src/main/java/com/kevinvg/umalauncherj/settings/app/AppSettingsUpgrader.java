package com.kevinvg.umalauncherj.settings.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.kevinvg.umalauncherj.util.Version;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppSettingsUpgrader {
    private AppSettingsUpgrader() {}

    public static void upgrade(JsonNode oldSettingsTree, AppSettings newSettingsBase) {
        log.info("Upgrading settings");
        var oldSettingsBase = oldSettingsTree.path("settings");
        Version oldVersion = new Version(oldSettingsBase.path("VERSION").path("value").asText());
        Version newVersion = new Version(newSettingsBase.getValue(AppSettings.SettingKey.VERSION).toString());

        log.info("Old version: {}", oldVersion);
        log.info("New version: {}", newVersion);

        int cmp = oldVersion.compareTo(newVersion);
        if (cmp < 0) {
            log.info("oldVersion < newVersion");
        } else if (cmp == 0) {
            log.info("oldVersion == newVersion");
        } else {
            log.info("oldVersion > newVersion");
        }

        log.info("Upgrade finished");
    }
}
