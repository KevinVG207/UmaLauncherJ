package com.kevinvg.umalauncherj.carrotjuicer;

import com.fasterxml.jackson.databind.JsonNode;
import com.kevinvg.umalauncherj.ui.UmaUiManager;
import com.kevinvg.umalauncherj.util.FileUtil;
import com.kevinvg.umalauncherj.util.MsgPackHandler;
import com.kevinvg.umalauncherj.packets.ResponsePacket;
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Singleton
public class CarrotJuicer {
    private static final Path messagesFolder;
    static {
        messagesFolder = FileUtil.getGameFolder().resolve("CarrotJuicer");
    }

    private final List<Path> ignoredPacketPaths = new ArrayList<>();

    private final CarrotJuicerTasks carrotJuicerTasks;
    private final CarrotJuicerTasks tasks;
    private final UmaUiManager ui;


    @Inject
    CarrotJuicer(CarrotJuicerTasks tasks, CarrotJuicerTasks carrotJuicerTasks, UmaUiManager ui) {
        this.tasks = tasks;
        this.carrotJuicerTasks = carrotJuicerTasks;
        this.ui = ui;
    }

    @Scheduled(every="0.5s", executionMaxDelay = "500ms", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    void processPackets() {
        log.info("Processing packets");
        var newPacketNames = getNewPacketNames();

        for (var path : newPacketNames) {
            this.processPacket(path);
        }

    }

    List<String> getNewPacketNames() {
        var files = new File(String.valueOf(messagesFolder)).listFiles();

        if (files == null) {
            System.err.println("Error listing files in CarrotJuicer directory");
            return new ArrayList<>();
        }

        return Stream.of(files)
                .filter(f -> !f.isDirectory())
                .sorted(Comparator.comparingLong(File::lastModified))
                .map(File::getName)
                .toList();
    }

    void processPacket(String packetName) {
        Path packetPath = messagesFolder.resolve(packetName);

        if (this.ignoredPacketPaths.contains(packetPath)) {
            return;
        }

        try {
            if (packetName.endsWith("R.msgpack")) {
                this.processResponse(packetPath);
            } else if (packetName.endsWith("Q.msgpack")) {
                this.processRequest(packetPath);
            } else {
                System.out.println("Packet name not valid: " + packetName);
            }
        } catch (Exception e) {
            // FIXME: Catching everything blech
            log.error("Error processing packet " + packetName);
            ui.showStacktraceDialog(e);
        }

        try {
            Files.deleteIfExists(packetPath);
        } catch (Exception e) {
            log.warn("Error deleting packet {}", packetName);
            this.ignoredPacketPaths.add(packetPath);
            new File(packetPath.toString()).deleteOnExit();  // Attempt to delete it later
        }

    }

    void processResponse(Path packetPath) {
        System.out.println("Processing response: " + packetPath);
        JsonNode root = MsgPackHandler.responseMsgpackToJsonNode(packetPath);
//        System.out.println(root);

        var dataNode = root.path("data");

        if (dataNode.isMissingNode() || !dataNode.isObject()) {
            System.err.println("Response packet does not contain 'data'");
            return;
        }

        System.out.println("Root node contains 'data'");

        carrotJuicerTasks.runTasks(new ResponsePacket(dataNode));
    }

    void processRequest(Path packetPath) {
        System.out.println("Processing request: " + packetPath);
        JsonNode root = MsgPackHandler.requestMsgPackToJsonNode(packetPath);
//        System.out.println(root);
    }
}