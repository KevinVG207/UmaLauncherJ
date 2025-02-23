package com.kevinvg.UmaLauncherJ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CarrotJuicer {
    private static final Path messagesFolder;
    static {
        messagesFolder = Util.getGameFolder().resolve("CarrotJuicer");
    }

    @Scheduled(fixedDelay = 1000)
    private void processPackets() {
        System.out.println("Processing packets");
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
        System.out.println(root);
    }

    void processRequest(Path packetPath) {
        System.out.println("Processing request: " + packetPath);
        JsonNode root = MsgPackHandler.requestMsgPackToJsonNode(packetPath);
        System.out.println(root);
    }
}