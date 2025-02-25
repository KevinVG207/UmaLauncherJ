package com.kevinvg.umalauncherj.helpertable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevinvg.umalauncherj.settings.AppSettings;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class HelperTable {
    ObjectMapper mapper = new ObjectMapper();

    private final AppSettings appSettings;

    @Inject
    HelperTable(AppSettings appSettings) {
        this.appSettings = appSettings;
    }

    @PostConstruct
    public void init(){
        // TODO: Load settings?
    }

    public String generateHtml(){
        System.out.println("HelperTable.generateHtml");

        var preset = new Preset();
        var presetAsMap = preset.toMap();
        String presetAsJson;
        try {
            presetAsJson = mapper.writerWithDefaultPrettyPrinter().withDefaultPrettyPrinter().writeValueAsString(presetAsMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println(presetAsJson);

        Preset loadedPreset = new Preset();
        loadedPreset.fromMap(presetAsMap);

        System.out.println("=== PRINTING APPSETTINGS ===");
        try {
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(appSettings.toMap()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        return loadedPreset.toString();
    }
}
