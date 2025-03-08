package com.kevinvg.umalauncherj.window;

import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import com.kevinvg.umalauncherj.tray.TrayIconController;
import com.kevinvg.umalauncherj.ui.UmaUiManager;
import com.kevinvg.umalauncherj.util.DmmUtil;
import com.kevinvg.umalauncherj.util.Orientation;
import com.kevinvg.umalauncherj.util.Rect;
import com.kevinvg.umalauncherj.util.Win32Util;
import com.kevinvg.umalauncherj.vpn.VpnManager;
import com.kevinvg.umalauncherj.window.domain.Window;
import com.sun.jna.platform.win32.WinDef;
import io.quarkus.runtime.Quarkus;
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

@Slf4j
@Singleton
public class GameWindowManager {
    private static final int SECONDS_UNTIL_SHUTDOWN = 15;

    private WinDef.HWND handle;
    private boolean hasExisted = false;
    private boolean needsToStart = true;
    private AppSettingsManager settings;
    private UmaUiManager ui;
    private Long closeNanos = null;
    private VpnManager vpnManager;
    private TrayIconController trayIcon;

    GameWindowManager() {
    }

    @Inject
    public GameWindowManager(AppSettingsManager settings, UmaUiManager ui, VpnManager vpnManager, TrayIconController trayIcon) {
        this.settings = settings;
        this.ui = ui;
        this.vpnManager = vpnManager;
        this.trayIcon = trayIcon;

        MenuItem maximizeItem = new MenuItem("Maximize & center game");
        ActionListener maximizeListener = e -> this.maximizeAndCenter();
        maximizeItem.addActionListener(maximizeListener);
        trayIcon.insertMenuItem(maximizeItem, 0);

        CheckboxMenuItem lockWindowItem = new CheckboxMenuItem("Lock game window");
        ItemListener lockWindowListener = e -> {
            boolean checked = ((CheckboxMenuItem) e.getSource()).getState();
            settings.set(AppSettings.SettingKey.LOCK_GAME_WINDOW, checked);
            if (checked && handle != null) {
                // Also save window
                saveCurrentWindowRect();
            }
        };
        lockWindowItem.setState(Boolean.TRUE.equals(settings.<Boolean>get(AppSettings.SettingKey.LOCK_GAME_WINDOW)));
        lockWindowItem.addItemListener(lockWindowListener);
        trayIcon.insertMenuItem(lockWindowItem, 0);
    }

    private void saveCurrentWindowRect() {
        var window = new Window(handle, settings);
        var rect = window.getRect();
        var orient = window.getOrientation();
        var key = (orient == Orientation.LANDSCAPE ? AppSettings.SettingKey.GAME_POSITION_LANDSCAPE : AppSettings.SettingKey.GAME_POSITION_PORTRAIT);
        settings.set(key, rect);
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
        handle = Win32Util.getGameHandle();

        if (handle == null && needsToStart && Boolean.TRUE.equals(settings.<Boolean>get(AppSettings.SettingKey.ENABLE_LAUNCH_GAME))) {
            launchGame();
        }

        needsToStart = false;
    }

    private void launchGame() {
        vpnManager.connect();
        boolean success = DmmUtil.launchGame();
        if (!success) {
            ui.showErrorDialog("Failed to start DMMGamePlayer.");
        }
    }

    private void gameJustClosed() {
        log.info("Game window stopped existing.");
        handle = null;
        closeNanos = System.nanoTime();
    }

    private void gameIsClosed() {
        if (closeNanos != null) {
            var diffNanos = System.nanoTime() - closeNanos;
            long cutoffNanos = SECONDS_UNTIL_SHUTDOWN * 1_000_000_000L;

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

        if (Boolean.TRUE.equals(settings.<Boolean>get(AppSettings.SettingKey.AUTOCLOSE_DMM))) {
            var dmmHandle = Win32Util.getDmmHandle();
            if (dmmHandle != null) {
                try {
                    Runtime.getRuntime().exec(new String[]{"taskkill", "/F", "/IM", Win32Util.DMM_EXECUTABLE});
                } catch (Exception e) {
                    log.error("Failed to close DMM", e);
                }
                //                Win32Util.sendCloseSignal(dmmHandle);
            }
        }

        if (Boolean.TRUE.equals(settings.<Boolean>get(AppSettings.SettingKey.VPN_DMM_ONLY))) {
            vpnManager.disconnect();
        }
    }

    private void gameExists() {
        if (Boolean.TRUE.equals(settings.<Boolean>get(AppSettings.SettingKey.LOCK_GAME_WINDOW))) {
            var window = new Window(handle, settings);

            var orientation = window.getOrientation();

            Rect savedRect;
            if (orientation == Orientation.PORTRAIT) {
                savedRect = settings.get(AppSettings.SettingKey.GAME_POSITION_PORTRAIT);
            } else {
                savedRect = settings.get(AppSettings.SettingKey.GAME_POSITION_LANDSCAPE);
            }

            if (!savedRect.isValid()) {
                maximizeAndCenter();
            } else {
                window.move(savedRect);
            }
        }
    }

    public boolean gameWindowExists() {
        return handle != null;
    }

    public void maximizeAndCenter() {
        if (handle == null) {
            return;
        }

        new Window(handle, settings).maximizeAndCenter();
        saveCurrentWindowRect();
    }
}
