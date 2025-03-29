package com.kevinvg.umalauncherj.ui.frames;

import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PreferencesFrame extends JFrame {
    private AppSettingsManager appSettingsManager;

    public PreferencesFrame(AppSettingsManager appSettingsManager) {
        super("Preferences");

        this.appSettingsManager = appSettingsManager;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                requestClose();
            }
        });

        add(new PreferencesPanel(), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void requestClose() {
        dispose();
    }

    class PreferencesPanel extends JPanel {
        public PreferencesPanel() {
            super(new GridLayout(1, 1));

            JTabbedPane tabbedPane = new JTabbedPane();

            tabbedPane.addTab("General", new SettingsPanel<>(appSettingsManager.getSettings()));

            add(tabbedPane);

            tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        }
    }
}
