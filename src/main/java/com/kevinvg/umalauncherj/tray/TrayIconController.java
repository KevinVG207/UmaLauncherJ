package com.kevinvg.umalauncherj.tray;

import com.kevinvg.umalauncherj.l18n.Localizer;
import com.kevinvg.umalauncherj.util.ResourcesUtil;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.Shutdown;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.ActionListener;

@Slf4j
@Singleton
public class TrayIconController {
    private TrayIcon trayIcon = null;
    private SystemTray systemTray = null;
    private final PopupMenu popupMenu = new PopupMenu();

    private static final String DEFAULT_TOOLTIP = "TRAY_DEFAULT_TOOLTIP";
    private static final String VPN_CONNECTING = "TRAY_VPN_CONNECTING";
    private static final String VPN_CONNECTED = "TRAY_VPN_CONNECTED";
    private static final String QUIT = "TRAY_QUIT";

    private static final String DEFAULT_ICON = "trayIcons/default.png";
    private static final String CONNECTING_ICON = "trayIcons/connecting.png";
    private static final String CONNECTED_ICON = "trayIcons/connected.png";

    private final Localizer loc;

    @Inject
    TrayIconController(Localizer loc) {
        this.loc = loc;

        // Setup
        if (!SystemTray.isSupported()) {
            log.error("SystemTray not supported");
            return;
        }

        systemTray = SystemTray.getSystemTray();

        Image image = ResourcesUtil.loadImageFromResources(DEFAULT_ICON);

        if (image == null) {
            throw new RuntimeException("Unable to load image from " + DEFAULT_ICON);
        }

        MenuItem defaultItem = new MenuItem(loc.get(QUIT));
        ActionListener listener = e -> {
            log.info(e.getActionCommand());
            this.quitAction();
        };
        defaultItem.addActionListener(listener);
        popupMenu.add(defaultItem);

        trayIcon = new TrayIcon(image, loc.get(DEFAULT_TOOLTIP), popupMenu);

        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertMenuItem(MenuItem menuItem, int index) {
        popupMenu.insert(menuItem, index);
    }

    private void quitAction() {
        Quarkus.asyncExit();
    }

    public void setConnecting() {
        if (trayIcon == null) {
            return;
        }

        trayIcon.setImage(ResourcesUtil.loadImageFromResources(CONNECTING_ICON));
        trayIcon.setToolTip(loc.get(VPN_CONNECTING));
    }

    public void setConnected() {
        if (trayIcon == null) {
            return;
        }

        trayIcon.setImage(ResourcesUtil.loadImageFromResources(CONNECTED_ICON));
        trayIcon.setToolTip(loc.get(DEFAULT_TOOLTIP) + " - " + loc.get(VPN_CONNECTED));
    }

    public void resetStatus() {
        if (trayIcon == null) {
            return;
        }

        trayIcon.setImage(ResourcesUtil.loadImageFromResources(DEFAULT_ICON));
        trayIcon.setToolTip(loc.get(DEFAULT_TOOLTIP));
    }

    @Shutdown
    void shutdown() {
        log.info("TrayIconController shutting down");
        if (systemTray != null && trayIcon != null) {
            systemTray.remove(trayIcon);
        }
    }
}
