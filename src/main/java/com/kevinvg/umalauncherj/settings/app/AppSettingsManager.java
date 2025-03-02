package com.kevinvg.umalauncherj.settings.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.kevinvg.umalauncherj.ui.UmaUiManager;
import com.kevinvg.umalauncherj.util.FileUtil;
import io.quarkus.runtime.Startup;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
@Singleton
public class AppSettingsManager {
    private static final String SETTINGS_FILENAME = "umasettings.json";

    private AppSettings settings;

    private ObjectMapper mapper = new ObjectMapper();
    private ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

    private UmaUiManager ui;

    @Inject
    AppSettingsManager(UmaUiManager ui) {
        this.ui = ui;
        this.settings = new AppSettings();
    }

    @Startup
    void loadSettings() {
        log.info("Loading settings");
        var settingsFile = getSettingsFile();
        if (!settingsFile.exists()) {
            log.info("No settings file to be loaded");
            return;
        }

        JsonNode loadedSettingsTree = null;
        try {
            loadedSettingsTree = mapper.readTree(settingsFile);
        } catch (Exception e) {
            // Log the error
            log.error("Error reading settings into tree.", e);
            return;
        }

        if (loadedSettingsTree == null || loadedSettingsTree.isNull() || loadedSettingsTree.isMissingNode()) {
            log.error("Error reading settings into tree.");
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

        var loadedSettingsMap = loadedSettings.getSettings();
        var curSettingsMap = this.settings.getSettings();
        for (var key : curSettingsMap.keySet()) {
            if (!loadedSettingsMap.containsKey(key)){
                loadedSettingsMap.put(key, curSettingsMap.get(key));
            }
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
            ui.showStacktraceDialog(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(AppSettings.SettingKey key) {
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

    public void set(AppSettings.SettingKey key, Object value) {
        log.info("Setting {} to {}", key, value);
        try {
            this.settings.setValue(key, value);
        } catch (Exception e) {
            ui.showStacktraceDialog(e);
            return;
        }

        saveSettings();
    }

    private static File getSettingsFile() {
        return FileUtil.getAppDataFile(SETTINGS_FILENAME);
    }
}
