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
//    https://stackoverflow.com/a/70884307
    Vertx vertx;
//    WorkerExecutor executor;

    @Inject
    protected UmaUiManager(Vertx vertx) {
        this.vertx = vertx;
    }

    @PostConstruct
    void init() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
//        this.executor = this.vertx.createSharedWorkerExecutor("UL-GUI-worker");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }

    private <T> void executeOnNewThread(Handler<Promise<T>> promiseHandler, Handler<AsyncResult<T>> resultHandler) {
        this.vertx.createSharedWorkerExecutor(UUID.randomUUID().toString()).executeBlocking(promiseHandler, resultHandler);
    }

    // https://docs.oracle.com/en/java/javase/21/docs/api/java.desktop/javax/swing/JOptionPane.html
    public void showErrorDialog(String message) {
        executeOnNewThread(
                (Promise<String> promise) -> {
                    JOptionPane.showMessageDialog(null, message, "Uma Launcher encountered an error", JOptionPane.ERROR_MESSAGE);
                    promise.complete("DIALOGUE CLOSED");
                },
                asyncResult -> log.info(asyncResult.result())
        );

//        this.vertx.<String>executeBlocking(promise -> {
//            JOptionPane.showMessageDialog(null, message, "Uma Launcher encountered an error", JOptionPane.ERROR_MESSAGE);
//            promise.complete("DIALOGUE CLOSED");
//        }, asyncResult -> log.info(asyncResult.result()));


    }

    public void showErrorDialog(Exception exception) {
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        this.showErrorDialog(sw.toString());
    }
}
