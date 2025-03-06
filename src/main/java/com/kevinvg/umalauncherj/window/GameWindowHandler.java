package com.kevinvg.umalauncherj.window;

import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import com.kevinvg.umalauncherj.ui.UmaUiManager;
import com.kevinvg.umalauncherj.util.DmmUtil;
import com.kevinvg.umalauncherj.util.Win32Util;
import com.sun.jna.platform.win32.WinDef;
import io.quarkus.runtime.Quarkus;
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class GameWindowHandler {
    private static final int SECONDS_UNTIL_SHUTDOWN = 15;

    private WinDef.HWND handle;
    private boolean hasExisted = false;
    private boolean needsToStart = true;
    private AppSettingsManager settings;
    private UmaUiManager ui;
    private Long closeNanos = null;

    GameWindowHandler() {
    }

    @Inject
    public GameWindowHandler(AppSettingsManager settings, UmaUiManager ui) {
        this.settings = settings;
        this.ui = ui;
    }

    @Scheduled(every = "0.5s", executionMaxDelay = "500ms", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    void checkForGameWindow() {
        // Trying to find the game for the first time
        if (!gameWindowExists() && !hasExisted) {
            findGameWindow();
            return;
        }

        // Game existed but has closed since
        if (!gameWindowExists()) {
            gameIsClosed();
            return;
        }

        if (!Win32Util.isWindow(handle)) {
            gameJustClosed();
            return;
        }

        if (!hasExisted) {
            gameJustDetected();
            return;
        }

        gameExists();
    }

    private void findGameWindow() {
        log.info("Checking for game window.");
        handle = Win32Util.getGameHandle();

        if (handle == null && needsToStart && Boolean.TRUE.equals(settings.<Boolean>get(AppSettings.SettingKey.ENABLE_LAUNCH_GAME))) {
            boolean success = DmmUtil.launchGame();
            if (!success) {
                ui.showErrorDialog("Failed to start DMMGamePlayer.");
            }
        }

        needsToStart = false;
    }

    private void gameJustClosed() {
        log.info("Game window stopped existing.");
        handle = null;
        closeNanos = System.nanoTime();
    }

    private void gameIsClosed() {
        log.info("Game window has closed.");
        if (closeNanos != null) {
            var diffNanos = System.nanoTime() - closeNanos;
            long cutoffNanos = ((long) SECONDS_UNTIL_SHUTDOWN) * 1_000_000_000;

            if (diffNanos > cutoffNanos) {
                closeNanos = null;
                hasExisted = false;
                Quarkus.asyncExit();
            }
        }
    }

    private void gameJustDetected() {
        log.info("Game window detected!");
        hasExisted = true;
        closeNanos = null;
    }

    private void gameExists() {
        log.info("Game window exists.");
    }

    public boolean gameWindowExists() {
        return handle != null;
    }
}
