package com.kevinvg.umalauncherj.richpresence;

import com.kevinvg.umalauncherj.Constants;
import com.kevinvg.umalauncherj.packets.ResponsePacket;
import com.kevinvg.umalauncherj.util.GameUtil;
import com.kevinvg.umalauncherj.util.UmapyoiUtil;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.arikia.dev.drpc.DiscordRichPresence;

@Slf4j
@Singleton
public class PresenceFactory {
    private static final long START_TIME = System.currentTimeMillis() / 1000L;
    private static final String DEFAULT_ICON = "umaicon";

    private final UmapyoiUtil umapyoiUtil;

    @Inject
    PresenceFactory(UmapyoiUtil umapyoiUtil) {
        this.umapyoiUtil = umapyoiUtil;
    }

    private DiscordRichPresence createPresence(String title, String descr, String bigImage, String bigImageText, String smallImage, String smallImageText) {
        return new DiscordRichPresence.Builder(descr)
                .setDetails(title)
                .setBigImage(bigImage, bigImageText)
                .setSmallImage(smallImage, smallImageText)
                .setStartTimestamps(START_TIME)
                .build();
    }

    public DiscordRichPresence defaultActivity() {
        return createPresence(
                "Launching game...",
                "Uma Launcher rich presence",
                DEFAULT_ICON,
                "It's Special Week!",
                "",
                ""
        );
    }

    public DiscordRichPresence trainingActivity(ResponsePacket response) {
        var charaInfo = response.getCharaInfo();

        String title = "Training - " + GameUtil.turnToString(charaInfo.path("turn").asInt());

        int cardId = charaInfo.path("card_id").asInt(100101);
        int charaId = Integer.parseInt(String.valueOf(cardId).substring(0, 4));
        String scenarioName = Constants.SCENARIO_NAMES_MAP.getOrDefault(
                charaInfo.path("scenario_id").asInt(-1),
                "You are now breathing manually."
        );

        int speed = charaInfo.path("speed").asInt();
        int stamina = charaInfo.path("stamina").asInt();
        int power = charaInfo.path("power").asInt();
        int guts = charaInfo.path("guts").asInt();
        int wit = charaInfo.path("wiz").asInt();
        int skillPt = charaInfo.path("skill_point").asInt();
        String descr = "%d %d %d %d %d | %d".formatted(speed, stamina, power, guts, wit, skillPt);

        // TODO: Fetch discord assets and use character icon.
        String bigImage = umapyoiUtil.getCharaIcon(charaId);
        String bigImageText = umapyoiUtil.getCharaName(charaId) + "\n" + umapyoiUtil.getOutfitName(cardId);

        return createPresence(title, descr, bigImage, bigImageText, DEFAULT_ICON, scenarioName);
    }
}
