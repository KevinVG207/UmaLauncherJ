package com.kevinvg.umalauncherj;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Util {
    private static Path gameFolder = null;

    private static ObjectMapper jsonMapper = new ObjectMapper();

    private static final String DMM_CONFIG_PATH = System.getenv("APPDATA") + "/dmmgameplayer5/dmmgame.cnf";

    private Util(){}

    public static Image loadImageFromResources(String path) {
        URL url = Util.class.getClassLoader().getResource(path);
        if (url == null) {
            System.err.println("Could not find resource: " + path);
            return null;
        }

        return new ImageIcon(url).getImage();
    }

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
}