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
    private final List<TrainingPartner> partners = new ArrayList<>();
    private int failureRate;
    private final Map<CommandType, Integer> gainedStats = new EnumMap<>(CommandType.class);
    private int gainedSkillPt;
    private int gainedEnergy;
    private int rainbowCount;
    private final List<ParamsIncDecInfo> paramsIncDecInfo = new ArrayList<>();

    public static CommandState fromCommandInfo(JsonNode commandInfo, JsonNode charaInfo) {
        var state = new CommandState();
        state.setCommandId(commandInfo.path("command_id").asInt());
        state.setSourceCommandType(commandInfo.path("command_type").asInt());
        CommandType cmdType = state.getCommandType();
        state.setCurrentStats(charaInfo.path(cmdType.internalName).asInt());
        state.setLevel(commandInfo.path("level").asInt());

        state.addParamsIncDecInfo(commandInfo.path("params_inc_dec_info_array"));

        // TODO: Training partners

        return state;
    }

    public void addParamsIncDecInfo(JsonNode paramsIncDecInfoArray) {
        if (paramsIncDecInfoArray == null || !paramsIncDecInfoArray.isArray()) {
            return;
        }

        for (var data : paramsIncDecInfoArray) {
            int targetType = data.path("target_type").asInt(-1);
            int value = data.path("value").asInt();
            if (targetType == -1) {
                continue;
            }

            paramsIncDecInfo.add(new ParamsIncDecInfo(targetType, value));
        }
    }

    public CommandType getCommandType() {
        return Constants.COMMAND_ID_TO_KEY.getOrDefault(String.valueOf(commandId), CommandType.UNKNOWN);
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