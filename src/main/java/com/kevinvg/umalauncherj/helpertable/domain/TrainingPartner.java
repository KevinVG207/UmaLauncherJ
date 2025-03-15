package com.kevinvg.umalauncherj.helpertable.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.kevinvg.umalauncherj.mdb.MdbService;
import com.kevinvg.umalauncherj.mdb.domain.SupportCard;
import jakarta.enterprise.inject.spi.CDI;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.StreamSupport;

public class TrainingPartner {
    private static final String GT_IMAGE_URL_TEMPLATE = "https://gametora.com/images/umamusume/characters/icons/chr_icon_%d.png";

    private @Getter final int id;
    private final int currentBond;
    private final JsonNode charaInfo;
    private int charaId = -1;
    private String imageUrl = "";
    private SupportCard supportCard = null;
    private @Getter int gainedBond = 0;
    private @Getter int gainedHintBond = 0;

    private TrainingPartner(int id, int currentBond, JsonNode charaInfo) {
        this.id = id;
        this.currentBond = currentBond;
        this.charaInfo = charaInfo;

        setupCharacter(id, charaInfo);
        setupBonds();
    }

    public static TrainingPartner fromNode(int id, JsonNode charaInfo) {
        var evaluationInfo = StreamSupport.stream(charaInfo.path("evaluation_info_array").spliterator(), false)
                .filter(n -> n.path("training_partner_id").asInt() == id).findFirst().orElseThrow();
        int currentBond = evaluationInfo.get("evaluation").asInt();
        return new TrainingPartner(id, currentBond, charaInfo);
    }

    public int getGainedUsefulBond() {
        return calcUsefulBond(gainedBond, currentBond);
    }

    public int getGainedUsefulHintBond() {
        return calcUsefulBond(gainedHintBond, currentBond + gainedBond);
    }

    public boolean isUseful() {
        return getGainedUsefulBond() > 0;
    }

    public boolean isSupportCard() {
        return supportCard != null;
    }

    private void setupCharacter(int id, JsonNode charaInfo) {
        if (id < 100) {
            var supportId = charaInfo.path("support_card_array").get(id - 1).path("support_card_id").asInt();
            var supportCard = CDI.current().select(MdbService.class).get().getSupportCardDict().get(supportId);
            if (supportCard != null) {
                this.supportCard = supportCard;
                this.charaId = supportCard.charaId();
                this.imageUrl = GT_IMAGE_URL_TEMPLATE.formatted(this.charaId);
            }
        } else if (id > 1000) {
            this.charaId = id;
            this.imageUrl = GT_IMAGE_URL_TEMPLATE.formatted(id);
        } else {
            this.charaId = CDI.current().select(MdbService.class).get().getSingleModeUniqueCharaDict().getOrDefault(charaInfo.path("scenario_id").asInt(), Map.of()).getOrDefault(this.id, -1);
            if (this.charaId != -1) {
                this.imageUrl = GT_IMAGE_URL_TEMPLATE.formatted(this.charaId);
            }
        }

        if (this.charaId == -1) {
            this.imageUrl = "https://umapyoi.net/missing_chara.png";
        }
    }
    
    private void setupBonds() {
        int maxPossible = Math.min(100, 100 - this.currentBond);

        int bond = 0;
        if (this.id < 1000) {
            bond = 7;
            if (isFriendGroupCard()) {
                bond = 4;
            }
        }

        bond += calcEffectBonusBond();
        bond = Math.min(bond, maxPossible);

        int hintBond = bond;

        if (this.id <= 6) {
            hintBond += 5 + calcEffectBonusBond();
        }
        hintBond = Math.min(hintBond, maxPossible) - bond;

        this.gainedBond = Math.max(0, bond);
        this.gainedHintBond = Math.max(0, hintBond);
    }

    private boolean isFriendGroupCard() {
        return this.id <= 6 && this.isSupportCard() && this.supportCard.isFriendGroup();
    }

    private int calcEffectBonusBond() {
        var charaEffectIdArray = this.charaInfo.path("chara_effect_id_array");
        if (charaEffectIdArray.isMissingNode() || charaEffectIdArray.isEmpty()) {
            return 0;
        }

        var charaEffectIds = StreamSupport.stream(charaEffectIdArray.spliterator(), false)
                .map(JsonNode::asInt).toList();

        if (this.id <= 6 && charaEffectIds.contains(8)) {
            return 2;
        }

        if (this.id == 102 && charaEffectIds.contains(9)) {
            return 2;
        }

        return 0;
    }

    private int calcUsefulBond(int gained, int starting) {
        int cutoff = 80;

        if (this.isSupportCard()) {
            if (getScenarioId() == 6 && Arrays.asList(10094, 30160).contains(supportCard.id())) {
                cutoff = 60;
            }
            else if (getScenarioId() == 7 && Arrays.asList(10104, 30188).contains(supportCard.id())) {
                cutoff = 60;
            }
            else if (this.supportCard.isFriendGroup()) {
                return 0;
            }
        }

        int bond = starting + gained;

        if (this.id > 6 && this.id <= 1000) {
            if (this.id == 102 && getScenarioId() != 6) {
                cutoff = 60;
            } else {
                return 0;
            }
        }

        return Math.max(0, Math.min(bond, cutoff) - starting);
    }

    private int getScenarioId() {
        return this.charaInfo.path("scenario_id").asInt();
    }
}
