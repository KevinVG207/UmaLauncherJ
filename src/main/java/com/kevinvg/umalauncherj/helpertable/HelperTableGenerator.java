package com.kevinvg.umalauncherj.helpertable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevinvg.umalauncherj.helpertable.domain.TrainingState;
import com.kevinvg.umalauncherj.helpertable.elements.Overlay;
import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Singleton
public class HelperTableGenerator {
    ObjectMapper mapper = new ObjectMapper();
    private final AppSettingsManager settings;

    @Setter
    @Getter
    private TrainingState trainingState;

    @Getter
    private TrainingState prevTrainingState;

    @Inject
    HelperTableGenerator(AppSettingsManager settings) {
        this.settings = settings;
    }

    public String generateHtml() {
        if (this.trainingState == null) {
            log.error("Attempted to generate Helper Table but trainingState is null!");
            return null;
        }

        // TODO: Implement preset selection by user
        List<Preset> presets = settings.get(AppSettings.SettingKey.TRAINING_HELPER_TABLE_PRESET_LIST);
        if (presets == null || presets.isEmpty()) {
            log.error("Attempted to generate Helper Table but no presets found!");
            return null;
        }

        Preset preset = presets.getFirst();

        String html = new Overlay(preset).generate(trainingState);

        if (trainingState != prevTrainingState) {
            prevTrainingState = trainingState;
        }

        return html;
    }

    public String generateHtml(TrainingState trainingState) {
        this.trainingState = trainingState;
        return generateHtml();
    }
}
