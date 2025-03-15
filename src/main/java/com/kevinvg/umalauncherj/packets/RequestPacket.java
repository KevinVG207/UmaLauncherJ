package com.kevinvg.umalauncherj.packets;

import com.fasterxml.jackson.databind.JsonNode;

public class RequestPacket {
    private final JsonNode data;

    public RequestPacket(JsonNode data) {
        this.data = data;
    }

    public boolean isAutoplay() {
        return data.path("exec_mode_value").asInt(0) == 1;
    }
}
