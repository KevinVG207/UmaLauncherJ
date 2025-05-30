package com.kevinvg.umalauncherj.packets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;

import java.util.HashMap;
import java.util.Map;

public class ResponsePacket {
    private final JsonNode data;

    public ResponsePacket(JsonNode data) {
        this.data = data;
    }

    public JsonNode getCharaInfo() {
        return getSingleModeKey("chara_info");
    }

    public JsonNode getTrainingEvent() {
        var eventArray = getSingleModeKey("unchecked_event_array");
        if (eventArray.isMissingNode() || !eventArray.isArray()) {
            return MissingNode.getInstance();
        }

        var eventData = eventArray.get(0);
        if (eventData == null) {
            return MissingNode.getInstance();
        }

        return eventData;
    }

    public JsonNode getSingleModeData() {
        var singleModeLoadCommon = data.path("single_mode_load_common");

        if (singleModeLoadCommon.isMissingNode()) {
            return data;
        }
        return singleModeLoadCommon;
    }

    private JsonNode getSingleModeKey(String key) {
        var singleModeLoadCommon = data.path("single_mode_load_common");

        if (singleModeLoadCommon.isMissingNode()) {
            return data.path(key);
        }

        return singleModeLoadCommon.path(key);
    }

    public Map<String, JsonNode> getSingleModeScenarioDataSets() {
        Map<String, JsonNode> out = new HashMap<>();
        for (var entry : data.properties()) {
            if (entry.getKey().endsWith("_data_set")) {
                out.put(entry.getKey(), entry.getValue());
            }
        }
        return out;
    }

    public boolean isTrainingRunTurn() {
        var charaInfo = getCharaInfo();
        var homeInfo = getSingleModeKey("home_info");
        if (homeInfo.isMissingNode()) {
            return false;
        }
        if (charaInfo.isMissingNode()) {
            return false;
        }
        return true;
    }

    public boolean isTrainingEnd() {
        var factorSelectNode = getSingleModeKey("single_mode_factor_select_common");
        return !factorSelectNode.isMissingNode();
    }

    public boolean isRaceStart() {
        var raceScenarioNode = getSingleModeKey("race_scenario");
        var raceStartInfoNode = getSingleModeKey("race_start_info");
        return !raceScenarioNode.isMissingNode() && !raceStartInfoNode.isMissingNode() && !raceScenarioNode.isEmpty() && !raceStartInfoNode.isEmpty();
    }

    public JsonNode getRaceStartInfo() {
        return getSingleModeKey("race_start_info");
    }

    public JsonNode getRaceHistory() {
        return getSingleModeKey("race_history");
    }
}
