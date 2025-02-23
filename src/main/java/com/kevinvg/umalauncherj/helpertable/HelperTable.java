package com.kevinvg.umalauncherj.helpertable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class HelperTable {
    ObjectMapper mapper = new ObjectMapper();

    HelperTable(){}

    @PostConstruct
    public void init(){

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

        return preset.toString();
    }
}
