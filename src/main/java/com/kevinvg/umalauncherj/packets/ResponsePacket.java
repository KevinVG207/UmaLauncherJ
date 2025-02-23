package com.kevinvg.umalauncherj.packets;

import com.fasterxml.jackson.databind.JsonNode;

public class ResponsePacket {
    private JsonNode charaInfo;

    public ResponsePacket(JsonNode data) {
        this.charaInfo = data.path("chara_info");
    }

    public JsonNode getCharaInfo() {
        return charaInfo;
    }
}
