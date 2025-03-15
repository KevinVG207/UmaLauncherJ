package com.kevinvg.umalauncherj.helpertable.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.kevinvg.umalauncherj.packets.ResponsePacket;
import lombok.Data;

import java.sql.Array;
import java.util.*;

@Data
public class TrainingState {
    private final int cardId;
    private final int charaId;
    private final int turn;
    private final int scenarioId;
    private final int energy;
    private final int maxEnergy;
    private final int fans;
    private final int skillPt;
    private final Map<CommandType, CommandState> commands;

    private final Map<String, JsonNode> scenarioNodes = new HashMap<>();

    // TODO: Other stuff

    public TrainingState(ResponsePacket responsePacket) {
        var charaInfo = responsePacket.getCharaInfo();

        this.cardId = charaInfo.path("card_id").asInt(100101);
        this.charaId = Integer.parseInt(String.valueOf(this.cardId).substring(0, 4));
        this.turn = charaInfo.path("turn").asInt();
        this.scenarioId = charaInfo.path("scenario_id").asInt();
        this.energy = charaInfo.path("vital").asInt();
        this.maxEnergy = charaInfo.path("max_vital").asInt();
        this.fans = charaInfo.path("fans").asInt();
        this.skillPt = charaInfo.path("skill_point").asInt();

        this.commands = new EnumMap<>(CommandType.class);

        var root = responsePacket.getSingleModeData();

        var homeInfo = root.path("home_info");

        for (var commandInfo : homeInfo.path("command_info_array")) {
            var commandState = CommandState.fromCommandInfo(commandInfo, charaInfo);
            commands.put(commandState.getCommandType(), commandState);
        }

        for (var entry : root.properties()) {
            if (entry.getKey().endsWith("_data_set")) {
                scenarioNodes.put(entry.getKey(), entry.getValue());
            }
        }

        for (var scenarioNode : scenarioNodes.values()) {
            var commandInfoArray = scenarioNode.path("command_info_array");
            if (commandInfoArray.isMissingNode() || commandInfoArray.isEmpty()) {
                continue;
            }
            for (var commandInfo : commandInfoArray) {
                var commandState = CommandState.fromCommandInfo(commandInfo, charaInfo);
                var commandType = commandState.getCommandType();
                if (commands.containsKey(commandType)) {
                    commands.get(commandType).mergeParamsIncDecInfo(commandState.getParamsIncDecInfo());
                }
            }
        }
    }

    public Map<CommandType, CommandState> getRelevantCommands() {
        Map<CommandType, CommandState> relevantCommands = new EnumMap<>(CommandType.class);
        for (var entry : commands.entrySet()) {
            if (entry.getKey() == CommandType.UNKNOWN) {
                continue;
            }
            relevantCommands.put(entry.getKey(), entry.getValue());
        }
        return relevantCommands;
    }
}
