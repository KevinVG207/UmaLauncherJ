package com.kevinvg.umalauncherj.richpresence;

import com.kevinvg.umalauncherj.Constants;
import com.kevinvg.umalauncherj.l18n.Localizer;
import com.kevinvg.umalauncherj.packets.ResponsePacket;
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
    private final Localizer loc;

    @Inject
    PresenceFactory(UmapyoiUtil umapyoiUtil, Localizer loc) {
        this.umapyoiUtil = umapyoiUtil;
        this.loc = loc;
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
                loc.get("PRESENCE_DEFAULT_TITLE"),
                loc.get("PRESENCE_DEFAULT_DESCR"),
                DEFAULT_ICON,
                loc.get("PRESENCE_DEFAULT_BIGTEXT"),
                "",
                ""
        );
    }

    public DiscordRichPresence trainingActivity(ResponsePacket response, boolean autoplay) {
        var charaInfo = response.getCharaInfo();

        String auto = autoplay ? "\uD83E\uDD16 " : "";  // Robot emoji
        String title = auto + loc.get("PRESENCE_TRAINING_TITLE") + " - " + loc.turnToString(charaInfo.path("turn").asInt());

        int cardId = charaInfo.path("card_id").asInt(100101);
        int charaId = Integer.parseInt(String.valueOf(cardId).substring(0, 4));
        String scenarioName = loc.get("SCENARIO_NAME_" + charaInfo.path("scenario_id").asText());

        int speed = charaInfo.path("speed").asInt();
        int stamina = charaInfo.path("stamina").asInt();
        int power = charaInfo.path("power").asInt();
        int guts = charaInfo.path("guts").asInt();
        int wit = charaInfo.path("wiz").asInt();
        int skillPt = charaInfo.path("skill_point").asInt();
        String descr = "%d %d %d %d %d | %d".formatted(speed, stamina, power, guts, wit, skillPt);

        String bigImage = umapyoiUtil.getCharaIcon(charaId);
        String bigImageText = umapyoiUtil.getCharaName(charaId) + "\n" + umapyoiUtil.getOutfitName(cardId);

        return createPresence(title, descr, bigImage, bigImageText, DEFAULT_ICON, scenarioName);
    }
}
