package com.kevinvg.umalauncherj.carrotjuicer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevinvg.umalauncherj.gametora.GtLanguage;
import com.kevinvg.umalauncherj.gametora.GtUtil;
import com.kevinvg.umalauncherj.helpertable.HelperTableGenerator;
import com.kevinvg.umalauncherj.helpertable.domain.TrainingState;
import com.kevinvg.umalauncherj.mdb.MdbService;
import com.kevinvg.umalauncherj.packets.RequestPacket;
import com.kevinvg.umalauncherj.packets.ResponsePacket;
import com.kevinvg.umalauncherj.richpresence.PresenceFactory;
import com.kevinvg.umalauncherj.richpresence.PresenceManager;
import com.kevinvg.umalauncherj.selenium.instances.GtEventHelper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Singleton
public class CarrotJuicerTasks {
    private final PresenceManager presenceManager;
    private final PresenceFactory presenceFactory;
    private final ObjectMapper objectMapper;
    private ResponsePacket prevResponse;
    private RequestPacket prevRequest;

    private HelperTableGenerator helperTableGenerator;
    private MdbService mdb;
    private GtEventHelper gtEventHelper;
    private CarrotJuicerUtil carrotJuicerUtil;

    private int previousRaceProgramId = -1;

    @Inject
    CarrotJuicerTasks(HelperTableGenerator helperTableGenerator, MdbService mdb, GtEventHelper gtEventHelper, PresenceManager presenceManager, PresenceFactory presenceFactory, ObjectMapper objectMapper, CarrotJuicerUtil carrotJuicerUtil) {
        this.helperTableGenerator = helperTableGenerator;
        this.mdb = mdb;
        this.gtEventHelper = gtEventHelper;
        this.presenceManager = presenceManager;
        this.presenceFactory = presenceFactory;
        this.objectMapper = objectMapper;
        this.carrotJuicerUtil = carrotJuicerUtil;
    }

    public void runTasks(ResponsePacket response) {
        gtEventHelper.closeEventResultPopups();

        if (response.isRaceStart()) {
            this.raceStartTask(response);
        }
        if (!response.getRaceHistory().isMissingNode()) {
            this.raceHistoryTask(response);
        }
        if (response.isTrainingRunTurn()) {
            this.trainingRunTask(response);
        }
        if (!response.getTrainingEvent().isMissingNode()) {
            this.trainingEventTask(response);
        }
        if (response.isTrainingEnd()) {
            this.trainingEndTask();
        }

        this.prevResponse = response;
    }

    public void runTasks(RequestPacket request) {
        this.prevRequest = request;
    }

    private void trainingRunTask(ResponsePacket response) {
        log.info("Training Run Task");
        presenceManager.setPresence(presenceFactory.trainingActivity(response));

        var charaInfo = response.getCharaInfo();

        String trainingId = charaInfo.path("start_time").asText();

        int cardId = charaInfo.path("card_id").asInt();
        int scenarioId = charaInfo.path("scenario_id").asInt();

        var supportIds = carrotJuicerUtil.supportIds(charaInfo);

        var url = GtUtil.makeHelperUrl(cardId, scenarioId, supportIds, GtLanguage.ENGLISH);

        gtEventHelper.setUrl(url);
        gtEventHelper.ensureTabOpen();
        gtEventHelper.updateOverlay(this.helperTableGenerator.generateHtml(new TrainingState(response)));
    }

    private void trainingEventTask(ResponsePacket response) {
        log.info("Training Event Task");
        var eventData = response.getTrainingEvent();
        var eventContentsInfo = eventData.path("event_contents_info");

        var choiceArray = eventContentsInfo.path("choice_array");
        if (choiceArray.isMissingNode() || choiceArray.isEmpty() || choiceArray.size() <= 1) {
            log.info("Event does not have choices");
            return;
        }

        if (eventIsExtraSupportCard(eventContentsInfo, response.getCharaInfo())) {
            gtEventHelper.selectExtraSupportCard(eventContentsInfo.path("support_card_id").asInt());
        }

        int eventId = eventData.path("event_id").asInt();
        List<String> eventTitles;
        if (CarrotJuicerConstants.AFTER_RACE_EVENT_IDS.contains(eventId)) {
            eventTitles = carrotJuicerUtil.getAfterRaceEventTitles(eventId, previousRaceProgramId);
        } else {
            int storyId = eventData.path("story_id").asInt();
            int cardId = response.getCharaInfo().path("card_id").asInt();
            eventTitles = mdb.getEventTitles(storyId, cardId);
        }

        log.info("Event Titles: {}", eventTitles);
        gtEventHelper.scrollToEventTitles(eventTitles);
    }

    private boolean eventIsExtraSupportCard(JsonNode eventContentsInfo, JsonNode charaInfo) {
        var supportCardIdNode = eventContentsInfo.path("support_card_id");
        if (supportCardIdNode.isMissingNode()){
            return false;
        }

        int supportCardId = supportCardIdNode.asInt();
        var supportIds = carrotJuicerUtil.supportIds(charaInfo);

        if (supportCardId == 0) {
            return false;
        }

        return !supportIds.contains(supportCardId);
    }

    private void trainingEndTask() {
        log.info("Training End Task");
        gtEventHelper.close();
    }

    private void raceStartTask(ResponsePacket response) {
        log.info("Race Start Task");
        var raceStartInfo = response.getRaceStartInfo();
        previousRaceProgramId = raceStartInfo.path("program_id").asInt(-1);
    }

    private void raceHistoryTask(ResponsePacket response) {
        log.info("Race History Task");
        var raceHistoryNode = response.getRaceHistory();

        if (raceHistoryNode.isMissingNode() || !raceHistoryNode.isArray() || raceHistoryNode.size() <= 1) {
            return;
        }

        previousRaceProgramId = raceHistoryNode.get(raceHistoryNode.size() - 1).path("program_id").asInt(-1);
    }
}
