package com.kevinvg.umalauncherj.util;

import com.kevinvg.umalauncherj.rest.client.UmapyoiService;
import com.kevinvg.umalauncherj.rest.client.domain.DiscordAsset;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Singleton
public class UmapyoiUtil {
    @RestClient
    UmapyoiService umapyoiService;

    private static final String FALLBACK_CHARA_ICON = "chara_0000";
    private static final String FALLBACK_MUSIC_ICON = "music_0000";
    private List<String> assetNames = new ArrayList<>();
    private Map<Integer, String> characterNames = new HashMap<>();
    private Map<Integer, String> outfitNames = new HashMap<>();

    @PostConstruct
    void init() {
        var assets = umapyoiService.getDiscordAssets();
        assetNames.clear();
        for (DiscordAsset asset : assets) {
            assetNames.add(asset.name());
        }

        var charaNames = umapyoiService.getCharacterNames();
        characterNames.clear();
        for (var data : charaNames) {
            characterNames.put(data.game_id(), data.name());
        }

        var outfitList = umapyoiService.getOutfits();
        outfitNames.clear();
        for (var data : outfitList) {
            outfitNames.put(data.id(), data.title());
        }
    }

    public String getCharaIcon(int charaId) {
        String key = "chara_" + charaId;
        if (!assetNames.contains(key)) {
            return FALLBACK_CHARA_ICON;
        }
        return key;
    }

    public String getMusicIcon(int musicId) {
        String key = "music_" + musicId;
        if (!assetNames.contains(key)) {
            return FALLBACK_MUSIC_ICON;
        }
        return key;
    }

    public String getCharaName(int charaId) {
        return characterNames.getOrDefault(charaId, String.valueOf(charaId));
    }

    public String getOutfitName(int cardId) {
        return outfitNames.getOrDefault(cardId, String.valueOf(cardId));
    }
}
