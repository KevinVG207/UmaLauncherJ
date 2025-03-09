package com.kevinvg.umalauncherj;

import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import com.kevinvg.umalauncherj.update.Updater;
import io.quarkus.runtime.Startup;
import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class StartupManager {
    private @Getter boolean started = false;
    private Updater updater;
    private AppSettingsManager settings;

    @Inject
    StartupManager(Updater updater, AppSettingsManager settings) {
        this.updater = updater;
        this.settings = settings;
    }

    @PostConstruct
    void startup() {
        updater.checkUpdate();
    }

    @Scheduled(every = "1s")
    void checkUpdateFinished() {
        if (started) {
            return;
        }

        if (updater.isDone() && settings.isLoaded()) {
            started = true;
        }
    }
}
