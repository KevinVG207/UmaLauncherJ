package com.kevinvg.umalauncherj.packets;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import io.quarkus.arc.WithCaching;

public class ResponsePacket {
    private JsonNode data;

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

    private JsonNode getSingleModeKey(String key) {
        var singleModeLoadCommon = data.path("single_mode_load_common");

        if (singleModeLoadCommon.isMissingNode()) {
            return data.path(key);
        }

        return singleModeLoadCommon.get(key);
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
