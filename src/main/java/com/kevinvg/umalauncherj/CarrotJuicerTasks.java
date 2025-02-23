package com.kevinvg.umalauncherj;

import com.fasterxml.jackson.databind.JsonNode;
import com.kevinvg.umalauncherj.gametora.GtLanguage;
import com.kevinvg.umalauncherj.gametora.GtUtil;
import com.kevinvg.umalauncherj.packets.RequestPacket;
import com.kevinvg.umalauncherj.packets.ResponsePacket;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarrotJuicerTasks {
    private ResponsePacket prevResponse;
    private RequestPacket prevRequest;

    public void runTasks(ResponsePacket response) {
        if (!response.getCharaInfo().isMissingNode()) {
            this.trainingRunTask(response);
        }

        this.prevResponse = response;
    }

    public void runTasks(RequestPacket request) {
        this.prevRequest = request;
    }

    public void trainingRunTask(ResponsePacket response) {
        System.out.println("Training Run Task");

        var charaInfo = response.getCharaInfo();

        String trainingId = charaInfo.path("start_time").asText();

        int cardId = charaInfo.path("card_id").asInt();
        int scenarioId = charaInfo.path("scenario_id").asInt();

        List<Integer> supportIds = new ArrayList<>();

        for (var supportCardData : charaInfo.path("support_card_array")) {
            supportIds.add(supportCardData.path("support_card_id").asInt());
        }

        var url = GtUtil.makeHelperUrl(cardId, scenarioId, supportIds, GtLanguage.ENGLISH);
        System.out.println("GT URL: " + url);
    }
}
