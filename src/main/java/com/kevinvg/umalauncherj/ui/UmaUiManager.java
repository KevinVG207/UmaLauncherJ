package com.kevinvg.umalauncherj.ui;

import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import com.kevinvg.umalauncherj.update.UpdateInfo;
import com.kevinvg.umalauncherj.update.Updater;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@Singleton
@Priority(999999)
public class UmaUiManager {
    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void showStacktraceDialog(String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, message, "Uma Launcher Error", JOptionPane.ERROR_MESSAGE));
    }

    public void showStacktraceDialog(Exception exception) {
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        this.showStacktraceDialog(sw.toString());
    }

    public void showErrorDialog(String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, message, "Uma Launcher Error", JOptionPane.ERROR_MESSAGE));
    }

    public void showInfoDialog(String title, String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE));
    }

    public void askForUpdate(UpdateInfo updateInfo, Updater updater, AppSettingsManager settings) {
        SwingUtilities.invokeLater(() -> {
            String[] possibleValues = {"Yes", "No", "Skip this version"};
            MessageWithLink message = new MessageWithLink("A new version of Uma Launcher was found.%nVersion: %s%s%n<a href=\"%s\">Release notes</a>%nUpdate now?".formatted(updateInfo.beta() ? "Pre-release " : "", updateInfo.version(), updateInfo.releaseNotesUrl()));
            JFrame frame = new JFrame();
            frame.setAlwaysOnTop(true);
            int selected = JOptionPane.showOptionDialog(frame, message, "Uma Launcher Update", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[1]);

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
}
