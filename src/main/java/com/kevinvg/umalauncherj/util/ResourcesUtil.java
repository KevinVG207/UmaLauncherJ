package com.kevinvg.umalauncherj.util;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
public class ResourcesUtil {
    private ResourcesUtil(){}

    public static URL getResource(String path){
        return ResourcesUtil.class.getClassLoader().getResource(path);
    }

    public static Image loadImageFromResources(String path) {
        URL url = getResource(path);
        if (url == null) {
            System.err.println("Could not find resource: " + path);
            return null;
        }

        return new ImageIcon(url).getImage();
    }

    public static boolean copyResourceToDevice(String resourcePath, Path targetPath) {
        log.info("Copying resource {} to {}", resourcePath, targetPath);
        boolean success = true;
        try (var resourceStream = ResourcesUtil.class.getClassLoader().getResourceAsStream(resourcePath)){
            if (resourceStream == null) {
                throw new RuntimeException("Could not find resource: " + resourcePath);
            }
            targetPath.toFile().mkdirs();
            Files.copy(resourceStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            log.error("Failed to copy", e);
            success = false;
        }
        return success;
    }
}