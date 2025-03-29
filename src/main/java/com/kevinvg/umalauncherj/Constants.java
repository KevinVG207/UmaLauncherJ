package com.kevinvg.umalauncherj;

import com.kevinvg.umalauncherj.helpertable.domain.CommandType;
import com.kevinvg.umalauncherj.helpertable.domain.ParamType;

import java.util.Map;

public class Constants {
    private Constants() {
    }

    public static final Map<String, CommandType> COMMAND_ID_TO_KEY = Map.ofEntries(
            Map.entry("101", CommandType.SPEED),
            Map.entry("105", CommandType.STAMINA),
            Map.entry("102", CommandType.POWER),
            Map.entry("103", CommandType.GUTS),
            Map.entry("106", CommandType.WIT),
            Map.entry("601", CommandType.SPEED),
            Map.entry("602", CommandType.STAMINA),
            Map.entry("603", CommandType.POWER),
            Map.entry("604", CommandType.GUTS),
            Map.entry("605", CommandType.WIT),
            Map.entry("901", CommandType.SPEED),
            Map.entry("902", CommandType.POWER),
            Map.entry("903", CommandType.GUTS),
            Map.entry("905", CommandType.STAMINA),
            Map.entry("906", CommandType.WIT),
            Map.entry("1101", CommandType.SPEED),
            Map.entry("1102", CommandType.STAMINA),
            Map.entry("1103", CommandType.POWER),
            Map.entry("1104", CommandType.GUTS),
            Map.entry("1105", CommandType.WIT),
            Map.entry("2101", CommandType.SPEED),
            Map.entry("2102", CommandType.STAMINA),
            Map.entry("2103", CommandType.POWER),
            Map.entry("2104", CommandType.GUTS),
            Map.entry("2105", CommandType.WIT),
            Map.entry("2201", CommandType.SPEED),
            Map.entry("2202", CommandType.STAMINA),
            Map.entry("2203", CommandType.POWER),
            Map.entry("2204", CommandType.GUTS),
            Map.entry("2205", CommandType.WIT),
            Map.entry("2301", CommandType.SPEED),
            Map.entry("2302", CommandType.STAMINA),
            Map.entry("2303", CommandType.POWER),
            Map.entry("2304", CommandType.GUTS),
            Map.entry("2305", CommandType.WIT),
            Map.entry("ss_match", CommandType.SS_MATCH)
    );

    public static final Map<Integer, ParamType> PARAM_ID_TO_TYPE = Map.ofEntries(
            Map.entry(1, ParamType.SPEED),
            Map.entry(2, ParamType.STAMINA),
            Map.entry(3, ParamType.POWER),
            Map.entry(4, ParamType.GUTS),
            Map.entry(5, ParamType.WIT),
            Map.entry(10, ParamType.ENERGY),
            Map.entry(30, ParamType.SKILLPT)
    );
}
