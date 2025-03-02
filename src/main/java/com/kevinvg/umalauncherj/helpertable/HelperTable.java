package com.kevinvg.umalauncherj.helpertable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class HelperTable {
    ObjectMapper mapper = new ObjectMapper();
//    {
//        mapper.registerModule(new ParanamerModule());
//    }

    private final AppSettingsManager settingsManager;

    @Inject
    HelperTable(AppSettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    @PostConstruct
    public void init(){
        // TODO: Load settings?
    }

    public String generateHtml(){
        System.out.println("HelperTable.generateHtml");

        var preset = new Preset();

        String presetAsJson;
        try {
            presetAsJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(preset);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

//        System.out.println(presetAsJson);

        Preset loadedPreset;
        try {
            loadedPreset = mapper.readValue(presetAsJson, Preset.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String presetAsJson2;
        try {
            presetAsJson2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(loadedPreset);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        boolean equals = presetAsJson.equals(presetAsJson2);
        assert(equals);

        if (equals) {
            System.out.println("Pre-serialized and post-serialized preset are identical!");
        }


        return preset.toString();
    }
}
