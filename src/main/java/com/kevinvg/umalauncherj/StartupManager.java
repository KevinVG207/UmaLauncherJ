package com.kevinvg.umalauncherj;

import com.kevinvg.umalauncherj.update.Updater;
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class StartupManager {
    private @Getter boolean started = false;
    private Updater updater;

    @Inject
    StartupManager(Updater updater) {
        this.updater = updater;
        updater.checkUpdate();
    }

    @Scheduled(every = "1s")
    void checkUpdateFinished() {
        if (started) {
            return;
        }

        if (updater.isDone()) {
            started = true;
        }
    }
}
