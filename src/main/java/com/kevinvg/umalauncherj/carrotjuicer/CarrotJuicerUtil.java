package com.kevinvg.umalauncherj.carrotjuicer;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class CarrotJuicerUtil {
    private CarrotJuicerUtil() {}

    public static List<Integer> supportIds(JsonNode charaInfo) {
        List<Integer> supportIds = new ArrayList<>();
        for (var supportCardData : charaInfo.path("support_card_array")) {
            supportIds.add(supportCardData.path("support_card_id").asInt());
        }
        return supportIds;
    }

    public static List<String> getAfterRaceEventTitles(int eventId) {
        List<String> out = new ArrayList<>();

        // TODO: Implement this
        out.add("RACE EVENT TITLES NOT YET IMPLEMENTED (" + eventId + ")");

        return out;
    }
}
