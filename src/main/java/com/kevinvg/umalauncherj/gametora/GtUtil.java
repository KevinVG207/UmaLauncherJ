package com.kevinvg.umalauncherj.gametora;

import java.util.List;

public class GtUtil {
    public static String makeHelperUrl(int cardId, int scenarioId, List<Integer> supportIds, GtLanguage language) {
        if (supportIds.size() < 6) {
            throw new RuntimeException("Cannot make helper url. supportIds.size() < 6. Is " + supportIds.size());
        }

        long segment1 = Long.parseLong(Long.toString(cardId) + scenarioId);
        long segment2 = Long.parseLong(Long.toString(supportIds.get(0)) + supportIds.get(1) + supportIds.get(2));
        long segment3 = Long.parseLong(Long.toString(supportIds.get(3)) + supportIds.get(4) + supportIds.get(5));


        return String.format("https://gametora.com%s/umamusume/training-event-helper?deck=%s-%s-%s",
                    language.urlSegment,
                    Long.toString(segment1, 36),
                    Long.toString(segment2, 36),
                    Long.toString(segment3, 36)
                ).toLowerCase();
    }
}
