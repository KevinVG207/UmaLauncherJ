package com.kevinvg.umalauncherj.carrotjuicer;

import com.fasterxml.jackson.databind.JsonNode;
import com.kevinvg.umalauncherj.util.FileUtil;
import com.kevinvg.umalauncherj.util.MsgPackHandler;
import com.kevinvg.umalauncherj.packets.ResponsePacket;
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Singleton
public class CarrotJuicer {
    private static final Path messagesFolder;
    static {
        messagesFolder = FileUtil.getGameFolder().resolve("CarrotJuicer");
    }

    private final CarrotJuicerTasks carrotJuicerTasks;

    CarrotJuicerTasks tasks;

    @Inject
    CarrotJuicer(CarrotJuicerTasks tasks, CarrotJuicerTasks carrotJuicerTasks) {
        this.tasks = tasks;
        this.carrotJuicerTasks = carrotJuicerTasks;
    }

    @Scheduled(every="0.5s")
    void processPackets() {
        System.out.println("Processing packets");
        var newPacketNames = getNewPacketNames();

        for (var path : newPacketNames) {
            try {
                this.processPacket(path);
            } catch (Exception e) {
                // FIXME: Catching everything blech
                System.out.println("Error processing packet " + path);
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
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

        if (packetName.endsWith("R.msgpack")) {
            this.processResponse(packetPath);
        } else if (packetName.endsWith("Q.msgpack")) {
            this.processRequest(packetPath);
        } else {
            System.out.println("Packet name not valid: " + packetName);
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