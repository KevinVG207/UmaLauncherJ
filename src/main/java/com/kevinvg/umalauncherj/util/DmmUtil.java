package com.kevinvg.umalauncherj.util;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class DmmUtil {
    private DmmUtil() {
    }

    public static boolean launchGame() {
        log.info("Starting game via DMM");
        try {
            Desktop.getDesktop().browse(new URI("dmmgameplayer://play/GCL/umamusume/cl/win"));
            return true;
        } catch (Exception e) {
            log.error("Unable to start DMMGamePlayer!", e);
            return false;
        }
    }
}
