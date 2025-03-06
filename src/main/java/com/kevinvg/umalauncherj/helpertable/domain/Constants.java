package com.kevinvg.umalauncherj.helpertable.domain;

import java.util.Map;

public class Constants {
    public static final Map<CommandType, String> TABLE_HEADERS = Map.of(
            CommandType.SPEED, "Speed",
            CommandType.STAMINA, "Stamina",
            CommandType.POWER, "Power",
            CommandType.GUTS, "Guts",
            CommandType.WIT, "Wit",
            CommandType.SS_MATCH, "SS Match"
    );
}
