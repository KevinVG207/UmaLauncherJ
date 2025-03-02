package com.kevinvg.umalauncherj.ui;

import io.vertx.core.*;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Singleton
public class UmaUiManager {

    @PostConstruct
    void init() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }

    // https://docs.oracle.com/en/java/javase/21/docs/api/java.desktop/javax/swing/JOptionPane.html
    public void showErrorDialog(String message) {
//        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE));
    }

    public void showErrorDialog(Exception exception) {
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        this.showErrorDialog(sw.toString());
    }
}
