package com.kevinvg.umalauncherj.carrotjuicer;

import com.fasterxml.jackson.databind.JsonNode;
import com.kevinvg.umalauncherj.gametora.GtUtil;
import com.kevinvg.umalauncherj.mdb.MdbService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Singleton
public class CarrotJuicerUtil {
    private final MdbService mdb;

    @Inject
    CarrotJuicerUtil(MdbService mdb) {
        this.mdb = mdb;
    }

    public List<Integer> supportIds(JsonNode charaInfo) {
        List<Integer> supportIds = new ArrayList<>();
        for (var supportCardData : charaInfo.path("support_card_array")) {
            supportIds.add(supportCardData.path("support_card_id").asInt());
        }
        return supportIds;
    }

    public List<String> getAfterRaceEventTitles(int eventId, int programId) {
        List<String> out = new ArrayList<>();

        if (programId == -1) {
            log.warn("Previous race grade unknown {}", programId);
            return out;
        }

        int raceGrade = mdb.getGradeFromProgramId(programId);

        if (raceGrade == -1) {
            log.warn("Race grade not found for programId {}", programId);
        }

        String gradeText;
        if (raceGrade > 300) {
            gradeText = "OP/Pre-OP";
        } else if (raceGrade > 100) {
            gradeText = "G2/G3";
        } else {
            gradeText = "G1";
        }

        out.add("%s %s".formatted(gradeText, GtUtil.EVENT_ID_TO_POS_STRING.getOrDefault(eventId, "UNKNOWN")));

        return out;
    }
}
