package com.kevinvg.umalauncherj.selenium;

import com.kevinvg.umalauncherj.util.FileUtil;
import com.kevinvg.umalauncherj.util.ResourcesUtil;
import io.quarkus.runtime.Startup;
import jakarta.inject.Singleton;

import java.io.File;
import java.util.List;

@Singleton
public class ProfileManager {
    private static final List<String> firefoxProfileFiles = List.of("FirefoxProfile/prefs.js", "FirefoxProfile/chrome/userChrome.css");
    public static final File firefoxProfileFile = FileUtil.getAppDataFile("FirefoxProfile");

    @Startup
    void init() {
        for (String path : firefoxProfileFiles) {
            ResourcesUtil.copyResourceToDevice(path, FileUtil.getAppDataFile(path).toPath());
        }

    }
}
