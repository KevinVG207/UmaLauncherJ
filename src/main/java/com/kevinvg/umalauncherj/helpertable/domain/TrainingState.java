package com.kevinvg.umalauncherj.helpertable.domain;

import com.kevinvg.umalauncherj.packets.ResponsePacket;
import lombok.Data;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

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
    private final List<CommandState> commands;

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

        this.commands = new ArrayList<>();

        var root = responsePacket.getSingleModeData();

        var homeInfo = root.path("home_info");

        for (var commandInfo : homeInfo.path("command_info_array")) {
            commands.add(CommandState.fromCommandInfo(commandInfo, charaInfo));
        }
    }

    public List<CommandState> getRelevantCommands() {
        List<CommandState> out = new ArrayList<>();
        for (var command : commands) {
            if (command.getCommandType() == CommandType.UNKNOWN) {
                continue;
            }
            out.add(command);
        }
        return out;
    }
}
