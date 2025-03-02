package com.kevinvg.umalauncherj;

import com.kevinvg.umalauncherj.richpresence.PresenceFactory;
import com.kevinvg.umalauncherj.richpresence.PresenceManager;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import com.kevinvg.umalauncherj.util.Win32Util;
import com.sun.jna.platform.win32.User32;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.awt.geom.Rectangle2D;

@Slf4j
@ApplicationScoped
public class Sandbox {
    AppSettingsManager settingsManager;
    PresenceManager presenceManager;

    @Inject
    protected Sandbox(AppSettingsManager settingsManager, PresenceManager presenceManager) {
        this.settingsManager = settingsManager;
        this.presenceManager = presenceManager;
    }

    @Startup
    public void init(@Observes StartupEvent ev) {
        log.info("Sandbox started");
    }

    @Scheduled(every = "10s", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    void saveSettings() {
        settingsManager.saveSettings();
        presenceManager.setPresence(PresenceFactory.defaultActivity());
    }

//    @Scheduled(every = "1s", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    void moveWindow() {
        var gameHandle = Win32Util.getGameHandle();

        if (gameHandle == null) {
            return;
        }

        var rect = Win32Util.getWindowRect(gameHandle).toRectangle();

        rect.x += 200;

        log.info("Moving window");
        Win32Util.moveWindow(gameHandle, (int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
    }
}
