package com.kevinvg.umalauncherj;

import com.kevinvg.umalauncherj.helpertable.domain.CommandType;

import java.util.Map;

public class Constants {
    private Constants() {
    }

    public static final Map<Integer, String> SCENARIO_NAMES_MAP = Map.of(
            1, "URA Finals",
            2, "Aoharu Cup",
            3, "Grand Live",
            4, "Make a New Track",
            5, "Grand Masters",
            6, "Project L'Arc",
            7, "U.A.F. Ready GO!",
            8, "Great Food Festival",
            9, "Run! Mecha Umamusume",
            10, "The Twinkle Legends"
    );

    public static final Map<CommandType, String> TABLE_HEADERS = Map.of(
            CommandType.SPEED, "Speed",
            CommandType.STAMINA, "Stamina",
            CommandType.POWER, "Power",
            CommandType.GUTS, "Guts",
            CommandType.WIT, "Wit",
            CommandType.SS_MATCH, "SS Match"
    );

    public static final Map<Integer, String> MONTH_MAP = Map.ofEntries(
            Map.entry(1, "January"),
            Map.entry(2, "February"),
            Map.entry(3, "March"),
            Map.entry(4, "April"),
            Map.entry(5, "May"),
            Map.entry(6, "June"),
            Map.entry(7, "July"),
            Map.entry(8, "August"),
            Map.entry(9, "September"),
            Map.entry(10, "October"),
            Map.entry(11, "November"),
            Map.entry(12, "December")
    );
}
