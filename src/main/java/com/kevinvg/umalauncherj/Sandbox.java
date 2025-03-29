package com.kevinvg.umalauncherj;

import com.kevinvg.umalauncherj.l18n.Localizer;
import com.kevinvg.umalauncherj.richpresence.PresenceFactory;
import com.kevinvg.umalauncherj.richpresence.PresenceManager;
import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import com.kevinvg.umalauncherj.ui.UmaUiManager;
import com.kevinvg.umalauncherj.util.FileUtil;
import com.kevinvg.umalauncherj.util.Win32Util;
import com.sun.jna.platform.win32.User32;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.awt.geom.Rectangle2D;

@Slf4j
@ApplicationScoped
public class Sandbox {
    private final Localizer localizer;
    private final UmaUiManager umaUiManager;
    AppSettingsManager settingsManager;
    PresenceManager presenceManager;

    @Inject
    protected Sandbox(AppSettingsManager settingsManager, PresenceManager presenceManager, Localizer localizer, UmaUiManager umaUiManager) {
        this.settingsManager = settingsManager;
        this.presenceManager = presenceManager;
        this.localizer = localizer;
        this.umaUiManager = umaUiManager;
    }

    @Startup
    void init(@Observes StartupEvent ev) {
        log.info("Sandbox started");
    }

//    @PostConstruct
//    void postConstruct() {
//        umaUiManager.showPreferencesDialog();
//    }

//    @Scheduled(every = "1s", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    void moveWindow() {
        var gameHandle = Win32Util.getGameHandle();

        if (gameHandle == null) {
            return;
        }

        var rect = Win32Util.getWindowRect(gameHandle);

        rect.setX(rect.getX() + 200);

        log.info("Moving window");
        Win32Util.moveWindow(gameHandle, rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }
}
