package com.kevinvg.umalauncherj.util;

import javax.swing.*;
import java.awt.Image;
import java.net.URL;

public class GenericUtil {
    private GenericUtil(){}

    public static Image loadImageFromResources(String path) {
        URL url = GenericUtil.class.getClassLoader().getResource(path);
        if (url == null) {
            System.err.println("Could not find resource: " + path);
            return null;
        }

        return new ImageIcon(url).getImage();
    }
}