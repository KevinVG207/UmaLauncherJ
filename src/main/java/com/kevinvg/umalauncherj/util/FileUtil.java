package com.kevinvg.umalauncherj.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    private static final String APPDATA_FOLDER = System.getenv("APPDATA") + "/UmaLauncherJ";
    private static final String DMM_CONFIG_PATH = System.getenv("APPDATA") + "/dmmgameplayer5/dmmgame.cnf";

    private static Path gameFolder = null;

    private static ObjectMapper jsonMapper = new ObjectMapper();

    private FileUtil() {}

    public static Path getGameFolder() {
        if (gameFolder != null) {
            return gameFolder;
        }

        JsonNode node;
        try {
            node = jsonMapper.readTree(new File(DMM_CONFIG_PATH));
        } catch (IOException e) {
            // TODO: Proper exception handling. Notify user and shut down.
            throw new RuntimeException(e);
        }

        var contents = node.path("contents");

        if (contents.isMissingNode() || !contents.isArray()) {
            throw new RuntimeException("DMM config contents not found");
        }

        for (var product : contents) {
            if (product.path("productId").asText().equals("umamusume")) {
                var tmp = product.path("detail").path("path");
                if (tmp.isTextual()) {
                    try {
                        gameFolder = Paths.get(tmp.asText());
                        break;
                    } catch (InvalidPathException ignored) {

                    }
                }
            }
        }

        if (gameFolder == null) {
            throw new RuntimeException("Unable to extract game install path from DMM!");
        }

        return gameFolder;
    }

    public static Path getAppDataPath() {
        return Paths.get(APPDATA_FOLDER);
    }

    public static File getAppDataFile(String path) {
        return new File(getAppDataPath().resolve(path).toAbsolutePath().toString());
    }
}
