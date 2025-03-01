package com.kevinvg.umalauncherj.settings.app;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.kevinvg.umalauncherj.util.FileUtil;
import io.quarkus.runtime.Startup;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

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

        AppSettings loadedSettings;
        try {
            loadedSettings = mapper.readValue(settingsFile, AppSettings.class);
        } catch (Exception e) {
            // Log the error
            log.error("Error loading settings.", e);
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

    private static File getSettingsFile() {
        return FileUtil.getAppDataFile(SETTINGS_FILENAME);
    }
}
