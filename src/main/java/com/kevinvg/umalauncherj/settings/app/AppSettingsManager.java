package com.kevinvg.umalauncherj.settings.app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.kevinvg.umalauncherj.settings.Setting;
import com.kevinvg.umalauncherj.ui.UmaUiManager;
import com.kevinvg.umalauncherj.util.FileUtil;
import io.quarkus.runtime.Shutdown;
import io.quarkus.runtime.Startup;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Singleton
public class AppSettingsManager {
    private static final String SETTINGS_FILENAME = "umasettings.json";

    private AppSettings settings;

    private ObjectMapper mapper = new ObjectMapper();
    private ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

    private UmaUiManager ui;

    private @Getter boolean loaded = false;

    @Inject
    AppSettingsManager(UmaUiManager ui) {
        this.ui = ui;
        this.settings = new AppSettings();

        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
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

        overwriteSettings(loadedSettingsTree);
        log.info("Settings loaded");
        saveSettings();
        loaded = true;
    }

    @Synchronized
    private void saveSettings() {
        log.info("Saving settings");
        var settingsFile = getSettingsFile();
        var tmpSettingsFile = new File(settingsFile + ".tmp");
        try {
            writer.writeValue(tmpSettingsFile, this.settings.getSettings());
            Files.move(tmpSettingsFile.toPath(), settingsFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
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

    private void overwriteSettings(JsonNode newSettings) {
        var currentSettingsMap = this.settings.getSettings();
        for (var entry : currentSettingsMap.entrySet()) {
            var key = entry.getKey();
            var keyString = key.toString();

            var newNode = newSettings.path(keyString);
            if (newNode.isMissingNode()) {
                log.warn("Key {} not found in old settings", keyString);
                continue;
            }

            var newValueNode = newNode.path("value");
            if (newValueNode.isMissingNode()) {
                log.warn("Value for key {} not found in old settings", keyString);
            }

            TypeReference<?> typeReference = currentSettingsMap.get(key).getTypeReference();
            Object newValue;
            try {
                newValue = mapper.convertValue(newValueNode, typeReference);
            } catch (Exception e) {
                log.error("Unable to convert loaded value for {} to TypeReference {}", key, typeReference, e);
                continue;
            }

            currentSettingsMap.get(key).setValue(newValue);
        }
    }

    @Shutdown(0)
    void shutdown() {
        saveSettings();
    }
}
