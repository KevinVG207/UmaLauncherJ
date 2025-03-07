package com.kevinvg.umalauncherj.ui;

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
}
