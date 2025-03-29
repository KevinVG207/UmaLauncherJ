package com.kevinvg.umalauncherj.ui;

import com.kevinvg.umalauncherj.l18n.Localizer;
import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import com.kevinvg.umalauncherj.ui.frames.PreferencesFrame;
import com.kevinvg.umalauncherj.update.UpdateInfo;
import com.kevinvg.umalauncherj.update.Updater;
import io.quarkus.runtime.Startup;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@Singleton
@Priority(999999)
public class UmaUiManager {
    private final Localizer loc;
    private @Setter AppSettingsManager appSettingsManager;

    @Inject
    public UmaUiManager(Localizer loc) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        this.loc = loc;
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }

    private void showStacktraceDialog(String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, message, loc.get("UI_ERROR_WINDOW_TITLE"), JOptionPane.ERROR_MESSAGE));
    }

    public void showStacktraceDialog(Exception exception) {
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        this.showStacktraceDialog(sw.toString());
    }

    public void showErrorDialog(String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, message, loc.get("UI_ERROR_WINDOW_TITLE"), JOptionPane.ERROR_MESSAGE));
    }

    public void showInfoDialog(String title, String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE));
    }

    public void askForUpdate(UpdateInfo updateInfo, Updater updater, AppSettingsManager settings) {
        SwingUtilities.invokeLater(() -> {
            String[] possibleValues = {loc.get("UI_UPDATE_ASK_YES"), loc.get("UI_UPDATE_ASK_NO"), loc.get("UI_UPDATE_ASK_SKIP")};
            MessageWithLink message = new MessageWithLink(loc.get("UI_UPDATE_MESSAGE").formatted(updateInfo.beta() ? loc.get("UI_UPDATE_MESSAGE_PRERELEASE") : "", updateInfo.version(), updateInfo.releaseNotesUrl()));
            JFrame frame = new JFrame();
            frame.setAlwaysOnTop(true);
            int selected = JOptionPane.showOptionDialog(frame, message, loc.get("UI_UPDATE_WINDOW_TITLE"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[1]);

            if (selected == -1) {
                updater.askForUpdateCallback(updateInfo, false);
                return;
            } else if (selected == 0) {
                updater.askForUpdateCallback(updateInfo, true);
                return;
            }

            if (selected == 2) {
                settings.set(AppSettings.SettingKey.SKIP_VERSION, updateInfo.version().toString());
            }
            updater.askForUpdateCallback(updateInfo, false);
        });
    }

    public void showUpdateDialog() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setAlwaysOnTop(true);
            frame.setUndecorated(true);
            frame.setTitle(loc.get("UI_UPDATE_WINDOW_TITLE"));
            frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            JLabel label = new JLabel(loc.get("UI_UPDATE_UPDATING"));
            label.setBorder(new CompoundBorder(label.getBorder(), new EmptyBorder(30, 20, 30, 20)));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            frame.add(label);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public void showPreferencesDialog() {
        SwingUtilities.invokeLater(() -> new PreferencesFrame(appSettingsManager));
    }
}
