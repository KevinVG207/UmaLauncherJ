package com.kevinvg.umalauncherj.helpertable.domain;

import lombok.Data;

@Data
public class TrainingPartner {
    private final int partnerId;
    private final int charaId;
    private final int startingBond;
    private final int gainedBond;
    private final int gainedUsefulBond;
}
