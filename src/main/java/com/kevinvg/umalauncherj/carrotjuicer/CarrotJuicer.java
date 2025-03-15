package com.kevinvg.umalauncherj.carrotjuicer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevinvg.umalauncherj.StartupManager;
import com.kevinvg.umalauncherj.packets.RequestPacket;
import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
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
    private static final long START_MILLIS = System.currentTimeMillis();
    private final ObjectMapper mapper;
    private final AppSettingsManager settings;
    private final StartupManager startupManager;
    private boolean checkForTimestamps = true;
    private static final Path messagesFolder;
    static {
        messagesFolder = FileUtil.getGameFolder().resolve("CarrotJuicer");
    }

    private final List<Path> ignoredPacketPaths = new ArrayList<>();

    private final CarrotJuicerTasks carrotJuicerTasks;
    private final UmaUiManager ui;


    @Inject
    CarrotJuicer(CarrotJuicerTasks carrotJuicerTasks, UmaUiManager ui, ObjectMapper mapper, AppSettingsManager settings, StartupManager startupManager) {
        this.carrotJuicerTasks = carrotJuicerTasks;
        this.ui = ui;
        this.mapper = mapper;
        this.settings = settings;
        this.startupManager = startupManager;
    }

    @Scheduled(every="0.5s", executionMaxDelay = "500ms", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    void processPackets() {
        if (!startupManager.isStarted()) return;

        log.info("Processing packets");
        var newPacketNames = getNewPacketNames();

        for (var path : newPacketNames) {
            this.processPacket(path);
        }

        if (checkForTimestamps) {
            checkForTimestamps = false;
        }
    }

    List<String> getNewPacketNames() {
        var files = new File(String.valueOf(messagesFolder)).listFiles();

        if (files == null) {
            log.error("Error listing files in CarrotJuicer directory");
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
            if (packetName.endsWith(".msgpack")) {
                if (checkForTimestamps) {
                    String timestampString = packetName.substring(0, packetName.length() - 9);
                    try {
                        long timestamp = Long.parseLong(timestampString);

                        if (timestamp < START_MILLIS) {
                            // Ignore any packets before UL start.
                            return;
                        }
                    } catch (NumberFormatException e) {
                        log.warn("Error parsing timestamp: {}", timestampString);
                        return;
                    }
                }

                if (packetName.endsWith("R.msgpack")) {
                    this.processResponse(packetPath);
                } else if (packetName.endsWith("Q.msgpack")) {
                    this.processRequest(packetPath);
                }
            } else {
                log.error("Packet name not valid: " + packetName);
            }
        } catch (Exception e) {
            // FIXME: Catching everything blech
            log.error("Error processing packet {}", packetName, e);
            ui.showStacktraceDialog(e);
        } finally {
            try {
                Files.deleteIfExists(packetPath);
            } catch (Exception e) {
                log.warn("Error deleting packet {}", packetName);
                this.ignoredPacketPaths.add(packetPath);
                new File(packetPath.toString()).deleteOnExit();  // Attempt to delete it later
            }
        }
    }

    void processResponse(Path packetPath) {
        log.info("Processing response: {}", packetPath);
        JsonNode root = MsgPackHandler.responseMsgpackToJsonNode(packetPath);

        if (Boolean.TRUE.equals(settings.<Boolean>get(AppSettings.SettingKey.WRITE_PACKETS))) {
            try {
                mapper.writerWithDefaultPrettyPrinter().writeValue(FileUtil.getAppDataFile("resp.json"), root);
            } catch (Exception e) {
                log.warn("Failed to write response packet JSON.");
            }
        }

        var dataNode = root.path("data");

        if (dataNode.isMissingNode() || !dataNode.isObject()) {
            log.error("Response packet does not contain 'data'");
            return;
        }

        carrotJuicerTasks.runTasks(new ResponsePacket(dataNode));
    }

    void processRequest(Path packetPath) {
        log.info("Processing request: {}", packetPath);
        JsonNode root = MsgPackHandler.requestMsgPackToJsonNode(packetPath);

        if (Boolean.TRUE.equals(settings.<Boolean>get(AppSettings.SettingKey.WRITE_PACKETS))) {
            try {
                mapper.writerWithDefaultPrettyPrinter().writeValue(FileUtil.getAppDataFile("req.json"), root);
            } catch (Exception e) {
                log.warn("Failed to write request packet JSON.");
            }
        }

        carrotJuicerTasks.runTasks(new RequestPacket(root));
    }
}