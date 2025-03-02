package com.kevinvg.umalauncherj.richpresence;

import de.jcm.discordgamesdk.activity.Activity;
import jakarta.inject.Singleton;

import java.time.Instant;

@Singleton
public class ActivityFactory {
    private static final Instant startInstant = Instant.now();

    private static Activity createActivity(String title, String descr, String largeImage, String largeText, String smallImage, String smallText) {
        Activity activity = new Activity();
        activity.timestamps().setStart(startInstant);

        activity.setDetails(title);
        activity.setState(descr);

        if (largeImage != null && !largeImage.isBlank()) {
            activity.assets().setLargeImage(largeImage);
        }
        if (largeText != null && !largeText.isBlank()) {
            activity.assets().setLargeText(largeText);
        }
        if (smallImage != null && !smallImage.isBlank()) {
            activity.assets().setSmallImage(smallImage);
        }
        if (smallText != null && !smallText.isBlank()) {
            activity.assets().setSmallText(smallText);
        }

        return activity;
    }

    public static Activity defaultActivity() {
        return createActivity(
                "Launching game...",
                "Ready your umapyois!",
                "umaicon",
                "It's Special Week!",
                "",
                ""
        );
    }
}
