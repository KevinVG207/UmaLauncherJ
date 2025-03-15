package com.kevinvg.umalauncherj.helpertable.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.kevinvg.umalauncherj.Constants;
import lombok.Data;

import java.util.*;

@Data
public class CommandState {
    private int commandId;
    private int sourceCommandType;
    private int currentStats;
    private int level;
    private final Map<Integer, TrainingPartner> partners = new HashMap<>();
    private int failureRate;
    private final Map<CommandType, Integer> gainedStats = new EnumMap<>(CommandType.class);
    private int gainedSkillPt;
    private int gainedEnergy;
    private int rainbowCount;
    private final Map<ParamType, Integer> paramsIncDecInfo = new EnumMap<>(ParamType.class);

    public static CommandState fromCommandInfo(JsonNode commandInfo, JsonNode charaInfo) {
        var state = new CommandState();
        state.setCommandId(commandInfo.path("command_id").asInt());
        state.setSourceCommandType(commandInfo.path("command_type").asInt());
        CommandType cmdType = state.getCommandType();
        state.setCurrentStats(charaInfo.path(cmdType.internalName).asInt());
        state.setLevel(commandInfo.path("level").asInt());

        state.addParamsIncDecInfo(commandInfo.path("params_inc_dec_info_array"));

        // TODO: Training partners
        state.addTrainingPartners(commandInfo.path("training_partner_array"), charaInfo);

        return state;
    }

    public void addParamsIncDecInfo(JsonNode paramsIncDecInfoArray) {
        if (paramsIncDecInfoArray == null || !paramsIncDecInfoArray.isArray()) {
            return;
        }

        for (var data : paramsIncDecInfoArray) {
            int targetTypeId = data.path("target_type").asInt(-1);
            int value = data.path("value").asInt();
            if (targetTypeId == -1) {
                continue;
            }

            var targetType = Constants.PARAM_ID_TO_TYPE.getOrDefault(targetTypeId, ParamType.UNKNOWN);

            int prevValue = paramsIncDecInfo.getOrDefault(targetType, 0);
            paramsIncDecInfo.put(targetType, prevValue + value);
        }
    }

    public void mergeParamsIncDecInfo(Map<ParamType, Integer> incDecInfo) {
        for (var entry : incDecInfo.entrySet()) {
            var targetType = entry.getKey();
            int value = entry.getValue() + paramsIncDecInfo.getOrDefault(targetType, 0);
            paramsIncDecInfo.put(targetType, value);
        }
    }

    public CommandType getCommandType() {
        return Constants.COMMAND_ID_TO_KEY.getOrDefault(String.valueOf(commandId), CommandType.UNKNOWN);
    }

    public void addTrainingPartners(JsonNode trainingPartnerArray, JsonNode charaInfo) {
        if (trainingPartnerArray == null || !trainingPartnerArray.isArray()) {
            return;
        }

        for (var positionNode : trainingPartnerArray) {
            int id = positionNode.asInt();
            TrainingPartner partner = TrainingPartner.fromNode(id, charaInfo);
            this.partners.put(partner.getId(), partner);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CommandState that)) return false;
        return getCommandType() == that.getCommandType();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCommandType());
    }
}