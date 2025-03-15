package com.kevinvg.umalauncherj.mdb.domain;

import java.util.Arrays;
import java.util.Map;

public record SupportCard(int id, int rarity, int commandId, int type, int charaId) {
    public SupportCardType getSupportCardType() {
        return MAPPING.getOrDefault(new int[]{commandId, type}, SupportCardType.UNKNOWN);
    }

    private static final Map<int[], SupportCardType> MAPPING = Map.ofEntries(
            Map.entry(new int[]{101, 1}, SupportCardType.SPEED),
            Map.entry(new int[]{105, 1}, SupportCardType.STAMINA),
            Map.entry(new int[]{102, 1}, SupportCardType.POWER),
            Map.entry(new int[]{103, 1}, SupportCardType.GUTS),
            Map.entry(new int[]{106, 1}, SupportCardType.WIT),
            Map.entry(new int[]{0, 2}, SupportCardType.FRIEND),
            Map.entry(new int[]{0, 3}, SupportCardType.GROUP)
    );

    public boolean isFriendGroup() {
        return Arrays.asList(SupportCardType.FRIEND, SupportCardType.GROUP).contains(getSupportCardType());
    }
}
