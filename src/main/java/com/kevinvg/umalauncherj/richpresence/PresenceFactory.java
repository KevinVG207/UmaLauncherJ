package com.kevinvg.umalauncherj.richpresence;

import jakarta.inject.Singleton;
import net.arikia.dev.drpc.DiscordRichPresence;

import java.time.Instant;

@Singleton
public class PresenceFactory {
    private static final long startTime = System.currentTimeMillis() / 1000L;

    private PresenceFactory() {}

    private static DiscordRichPresence createPresence(String title, String descr, String bigImage, String bigImageText, String smallImage, String smallImageText) {
        return new DiscordRichPresence.Builder(descr)
                .setDetails(title)
                .setBigImage(bigImage, bigImageText)
                .setSmallImage(smallImage, smallImageText)
                .setStartTimestamps(startTime)
                .build();
    }

    public static DiscordRichPresence defaultActivity() {
        return createPresence(
                "Launching game...",
                "Uma Launcher rich presence",
                "umaicon",
                "It's Special Week!",
                "",
                ""
        );
    }
}
