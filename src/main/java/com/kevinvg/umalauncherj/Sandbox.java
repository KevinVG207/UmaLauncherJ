package com.kevinvg.umalauncherj;

import com.kevinvg.umalauncherj.richpresence.ActivityFactory;
import com.kevinvg.umalauncherj.richpresence.PresenceManager;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import com.kevinvg.umalauncherj.util.Win32Util;
import com.sun.jna.platform.win32.User32;
import de.jcm.discordgamesdk.user.Presence;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

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

    @Scheduled(every = "10s")
    void saveSettings() {
        settingsManager.saveSettings();
        presenceManager.setActivity(ActivityFactory.defaultActivity());
    }

//    @Scheduled(every = "1s")
    void moveWindow() {
        var gameHandle = Win32Util.getGameHandle();

        if (gameHandle == null) {
            return;
        }

        User32 user32 = User32.INSTANCE;

        var rect = Win32Util.getWindowRect(gameHandle);

        rect.left += 200;
        rect.right += 200;

        System.out.println("Moving window");
        Win32Util.moveWindow(gameHandle, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
    }
}
