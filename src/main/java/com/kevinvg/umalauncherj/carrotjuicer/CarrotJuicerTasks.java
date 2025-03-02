package com.kevinvg.umalauncherj.carrotjuicer;

import com.fasterxml.jackson.databind.JsonNode;
import com.kevinvg.umalauncherj.gametora.GtLanguage;
import com.kevinvg.umalauncherj.gametora.GtUtil;
import com.kevinvg.umalauncherj.helpertable.HelperTable;
import com.kevinvg.umalauncherj.mdb.MdbService;
import com.kevinvg.umalauncherj.packets.RequestPacket;
import com.kevinvg.umalauncherj.packets.ResponsePacket;
import com.kevinvg.umalauncherj.selenium.Horsium;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Singleton
public class CarrotJuicerTasks {
    private ResponsePacket prevResponse;
    private RequestPacket prevRequest;

    private HelperTable helperTable;
    private MdbService mdb;
    private Horsium horsium;

    @Inject
    CarrotJuicerTasks(HelperTable helperTable, MdbService mdb, Horsium horsium) {
        this.helperTable = helperTable;
        this.mdb = mdb;
        this.horsium = horsium;
    }

    public void runTasks(ResponsePacket response) {
        horsium.closeEventResultPopups();

        if (!response.getCharaInfo().isMissingNode()) {
            this.trainingRunTask(response);
        }
        if (!response.getTrainingEvent().isMissingNode()) {
            this.trainingEventTask(response);
        }

        this.prevResponse = response;
    }

    public void runTasks(RequestPacket request) {
        this.prevRequest = request;
    }

    private void trainingRunTask(ResponsePacket response) {
        log.info("Training Run Task");

        var charaInfo = response.getCharaInfo();

        String trainingId = charaInfo.path("start_time").asText();

        int cardId = charaInfo.path("card_id").asInt();
        int scenarioId = charaInfo.path("scenario_id").asInt();

        var supportIds = CarrotJuicerUtil.supportIds(charaInfo);

        var url = GtUtil.makeHelperUrl(cardId, scenarioId, supportIds, GtLanguage.ENGLISH);
        log.info("GT URL: {}", url);

        horsium.setGameToraUrl(url);
        horsium.ensureTabOpen();

        this.helperTable.generateHtml();
    }

    private void trainingEventTask(ResponsePacket response) {
        log.info("Training Event Task");
        var eventData = response.getTrainingEvent();
        var eventContentsInfo = eventData.path("event_contents_info");

        var choiceArray = eventContentsInfo.path("choice_array");
        if (choiceArray.isMissingNode()){
            log.info("Event does not have choices");
            return;
        }

        if (eventIsExtraSupportCard(eventContentsInfo, response.getCharaInfo())) {
            horsium.selectExtraSupportCard(eventContentsInfo.path("support_card_id").asInt());
        }

        int eventId = eventData.path("event_id").asInt();
        List<String> eventTitles;
        if (CarrotJuicerConstants.AFTER_RACE_EVENT_IDS.contains(eventId)) {
            eventTitles = CarrotJuicerUtil.getAfterRaceEventTitles(eventId);
        } else {
            int storyId = eventData.path("story_id").asInt();
            int cardId = response.getCharaInfo().path("card_id").asInt();
            eventTitles = mdb.getEventTitles(storyId, cardId);
        }

        log.info("Event Titles: {}", eventTitles);
        horsium.scrollToEventTitles(eventTitles);
    }

    private boolean eventIsExtraSupportCard(JsonNode eventContentsInfo, JsonNode charaInfo) {
        var supportCardIdNode = eventContentsInfo.path("support_card_id");
        if (supportCardIdNode.isMissingNode()){
            return false;
        }

        int supportCardId = supportCardIdNode.asInt();
        var supportIds = CarrotJuicerUtil.supportIds(charaInfo);

        if (supportCardId == 0) {
            return false;
        }

        return !supportIds.contains(supportCardId);
    }
}
