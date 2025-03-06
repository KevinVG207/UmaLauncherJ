package com.kevinvg.umalauncherj.helpertable.domain;

import lombok.Data;

import java.util.List;

@Data
public class CommandState {
    private final CommandType commandType;
    private final int currentStats;
    private final int level;
    private final List<TrainingPartner> partners;
    private final int failureRate;
    private final int gainedStats;
    private final int gainedSkillPt;
//    private final int totalBond;  // These two might come out of partner objects?
//    private final int usefulBond;
    private int gainedEnergy;
    private int rainbowCount;
}