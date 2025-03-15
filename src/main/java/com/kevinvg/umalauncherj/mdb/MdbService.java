package com.kevinvg.umalauncherj.mdb;

import com.kevinvg.umalauncherj.mdb.domain.SupportCard;
import com.kevinvg.umalauncherj.ui.UmaUiManager;
import io.quarkus.cache.CacheResult;
import io.quarkus.runtime.Shutdown;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Slf4j
@Singleton
public class MdbService {
    private static final String JDBC_URL = "jdbc:sqlite:%s/AppData/LocalLow/Cygames/umamusume/master/master.mdb".formatted(System.getenv("USERPROFILE"));
    private Connection conn;

    private UmaUiManager ui;

    @Inject
    MdbService(UmaUiManager ui) {
        this.ui = ui;

        try {
            var config = new Properties();
            config.setProperty("open_mode", "1");  // TODO: Does this work?
            this.conn = DriverManager.getConnection(JDBC_URL, config);
            log.info("Connected to mdb database");
        } catch (SQLException e) {
            ui.showStacktraceDialog(e);
        }
    }

    @Shutdown
    void disconnect() throws SQLException {
        conn.close();
    }

    public List<String> getEventTitles(int storyId, int cardId) {
        int normalizedStoryId = convertShortStoryId(storyId);

        String storyIdString = String.valueOf(normalizedStoryId);

        List<String> eventTitles;
        if (storyIdString.startsWith("40")) {
            eventTitles = getEventTitlesSpecial(normalizedStoryId, cardId);
        } else {
            eventTitles = getEventTitlesDefault(normalizedStoryId);
        }

        if (eventTitles.isEmpty()) {
            log.warn("Event Titles empty for choice event {}", storyId);
        }

        return eventTitles;
    }

    private int convertShortStoryId(int storyId) {
        try (var stmt = conn.prepareStatement("SELECT story_id FROM single_mode_story_data WHERE short_story_id = ? LIMIT 1")) {
            stmt.setInt(1, storyId);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            log.error("Exception fetching short story id for {}", storyId);
        }
        return storyId;
    }

    private List<String> getEventTitlesDefault(int storyId) {
        List<String> eventTitles = new ArrayList<>();
        try (var stmt = conn.prepareStatement("SELECT text FROM text_data WHERE category = 181 AND \"index\" = ? LIMIT 1")) {
            stmt.setInt(1, storyId);
            var rs = stmt.executeQuery();
            if (!rs.next()) {
                log.warn("Could not fetch event titles {}", storyId);
                return eventTitles;
            }
            eventTitles.add(rs.getString(1));
        } catch (SQLException e) {
            log.error("Exception fetching event titles for {}", storyId);
        }

        return eventTitles;
    }

    private List<String> getEventTitlesSpecial(int storyId, int cardId) {
        List<String> eventTitles = getEventTitlesDefault(storyId);

        int dressIcon;
        try (var stmt = conn.prepareStatement("SELECT event_title_dress_icon FROM single_mode_story_data WHERE story_id = ? AND card_id = ? LIMIT 1")) {
            stmt.setInt(1, storyId);
            stmt.setInt(2, cardId);
            var rs = stmt.executeQuery();
            if (!rs.next()) {
                return eventTitles;
            }
            dressIcon = rs.getInt(1);
        } catch (SQLException e) {
            log.warn("Exception fetching event_title_dress_icon for {}", storyId);
            return eventTitles;
        }

        if (dressIcon == 0) {
            return eventTitles;
        }

        try (var stmt = conn.prepareStatement("SELECT story_id FROM single_mode_story_data WHERE event_title_dress_icon = ? ORDER BY id")) {
            stmt.setInt(1, dressIcon);
            var rs = stmt.executeQuery();

            List<Integer> defaultIds = new ArrayList<>();
            List<Integer> larcIds = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt(1);
                String idString = String.valueOf(id);
                if (idString.startsWith("40")) {
                    larcIds.add(id);
                } else if (idString.startsWith("50")) {
                    defaultIds.add(id);
                }
            }

            var idx = defaultIds.indexOf(storyId);

            if (idx == -1) {
                return eventTitles;
            }

            idx %= defaultIds.size();
            eventTitles.addAll(getEventTitlesDefault(defaultIds.get(idx)));

            return eventTitles;
        } catch (SQLException e) {
            log.warn("Exception fetching story_id using dressIcon {}", dressIcon);
            return eventTitles;
        }
    }

    public int getGradeFromProgramId(int programId) {
        try (var stmt = conn.prepareStatement("SELECT r.grade FROM single_mode_program smp JOIN race_instance ri on smp.race_instance_id = ri.id JOIN race r on ri.race_id = r.id WHERE smp.id = ? LIMIT 1;")) {
            stmt.setInt(1, programId);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            log.error("Exception fetching race grade for {}", programId);
        }
        return -1;
    }

    @CacheResult(cacheName = "SupportCardDict")
    public Map<Integer, SupportCard> getSupportCardDict() {
        try(var stmt = conn.prepareStatement("SELECT id, rarity, command_id, support_card_type, chara_id FROM support_card_data")) {
            var rs = stmt.executeQuery();
            Map<Integer, SupportCard> supportCardDict = new HashMap<>();
            while (rs.next()) {
                int id = rs.getInt(1);
                int rarity = rs.getInt(2);
                int commandId = rs.getInt(3);
                int supportCardType = rs.getInt(4);
                int charaId = rs.getInt(5);
                supportCardDict.put(id, new SupportCard(id, rarity, commandId, supportCardType, charaId));
            }
            return supportCardDict;
        } catch (SQLException e) {
            log.error("Exception fetching support card dict");
            return Map.of();
        }
    }

    @CacheResult(cacheName = "SingleModeUniqueCharaDict")
    public Map<Integer, Map<Integer, Integer>> getSingleModeUniqueCharaDict() {
        try (var stmt = conn.prepareStatement("SELECT scenario_id, partner_id, chara_id FROM single_mode_unique_chara")) {
            Map<Integer, Map<Integer, Integer>> singleModeUniqueCharaDict = new HashMap<>();
            var rs = stmt.executeQuery();
            while (rs.next()) {
                int scenarioId = rs.getInt(1);
                int partnerId = rs.getInt(2);
                int charaId = rs.getInt(3);
                singleModeUniqueCharaDict.putIfAbsent(scenarioId, new HashMap<>());
                singleModeUniqueCharaDict.get(scenarioId).put(partnerId, charaId);
            }
            return singleModeUniqueCharaDict;
        } catch (SQLException e) {
            log.error("Exception fetching single mode unique chara dict");
            return Map.of();
        }
    }
}
