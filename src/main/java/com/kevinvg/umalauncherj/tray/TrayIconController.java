package com.kevinvg.umalauncherj.tray;

import com.kevinvg.umalauncherj.util.ResourcesUtil;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.Shutdown;
import io.quarkus.runtime.Startup;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

@Slf4j
@Singleton
public class TrayIconController {
    private TrayIcon trayIcon = null;
    private SystemTray systemTray = null;

    private static final String DEFAULT_TOOLTIP = "Uma Launcher";

    private static final String DEFAULT_ICON = "trayIcons/default.png";
    private static final String CONNECTING_ICON = "trayIcons/connecting.png";
    private static final String CONNECTED_ICON = "trayIcons/connected.png";

    @Startup
    void init() {
        if (!SystemTray.isSupported()) {
            log.error("SystemTray not supported");
            return;
        }

        systemTray = SystemTray.getSystemTray();

        log.info(new File(DEFAULT_ICON).getAbsolutePath());

        Image image = ResourcesUtil.loadImageFromResources(DEFAULT_ICON);

        if (image == null) {
            throw new RuntimeException("Unable to load image from " + DEFAULT_ICON);
        }

        ActionListener listener = e -> {
            log.info(e.getActionCommand());
            this.quitAction();
        };

        PopupMenu popupMenu = new PopupMenu();

        MenuItem defaultItem = new MenuItem("Quit");
        defaultItem.addActionListener(listener);
        popupMenu.add(defaultItem);

        trayIcon = new TrayIcon(image, DEFAULT_TOOLTIP, popupMenu);

        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    private void quitAction() {
        Quarkus.asyncExit();
    }

    public void setConnecting() {
        if (trayIcon == null) {
            return;
        }

        trayIcon.setImage(ResourcesUtil.loadImageFromResources(CONNECTING_ICON));
        trayIcon.setToolTip("Connecting to VPN...");
    }

    public void setConnected() {
        if (trayIcon == null) {
            return;
        }

        trayIcon.setImage(ResourcesUtil.loadImageFromResources(CONNECTED_ICON));
        trayIcon.setToolTip("UmaLauncher - VPN Connected");
    }

    public void resetStatus() {
        if (trayIcon == null) {
            return;
        }

        trayIcon.setImage(ResourcesUtil.loadImageFromResources(DEFAULT_ICON));
        trayIcon.setToolTip(DEFAULT_TOOLTIP);
    }

    @Shutdown
    void shutdown() {
        log.info("TrayIconController shutting down");
        if (systemTray != null && trayIcon != null) {
            systemTray.remove(trayIcon);
        }
    }
}
