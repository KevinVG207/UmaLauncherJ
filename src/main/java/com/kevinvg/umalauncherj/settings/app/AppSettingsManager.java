package com.kevinvg.umalauncherj.settings.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.kevinvg.umalauncherj.util.FileUtil;
import io.quarkus.runtime.Startup;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
@Singleton
public class AppSettingsManager {
    private static final String SETTINGS_FILENAME = "umasettings.json";

    private AppSettings settings = new AppSettings();

    private ObjectMapper mapper = new ObjectMapper();
    private ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

    @Startup
    void loadSettings() {
        log.info("Loading settings");
        var settingsFile = getSettingsFile();
        if (!settingsFile.exists()) {
            log.info("No settings file to be loaded");
            return;
        }

        JsonNode loadedSettingsTree;
        try {
            loadedSettingsTree = mapper.readTree(settingsFile);
        } catch (Exception e) {
            // Log the error
            log.error("Error reading settings into tree.", e);
            return;
        }

        AppSettingsUpgrader.upgrade(loadedSettingsTree, settings);

        AppSettings loadedSettings;
        try {
            loadedSettings = mapper.convertValue(loadedSettingsTree, AppSettings.class);
        } catch (IllegalArgumentException e) {
            log.error("Error converting settings tree to AppSettings", e);
            return;
        }

        this.settings = loadedSettings;

        log.info("Settings loaded");
    }

    public void saveSettings() {
        log.info("Saving settings");
        var settingsFile = getSettingsFile();
        try {
            writer.writeValue(settingsFile, this.settings);
            log.info("Settings saved");
        } catch (Exception e) {
            log.error("Error saving settings.", e);
            // TODO: Make an error popup!
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getSetting(AppSettings.SettingKey key) {
        Object value = this.settings.getValue(key);

        if (value == null) {
            log.error("Unable to fetch {} from AppSettings", key);
            return null;
        }

        T result;
        try {
            result = (T) value;
        } catch (ClassCastException e) {
            log.error("Unable to cast {} from AppSettings", key, e);
            return null;
        }

        return result;
    }

    private static File getSettingsFile() {
        return FileUtil.getAppDataFile(SETTINGS_FILENAME);
    }
}
