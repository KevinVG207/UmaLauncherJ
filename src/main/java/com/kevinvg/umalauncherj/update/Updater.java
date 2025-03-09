package com.kevinvg.umalauncherj.update;

import com.kevinvg.umalauncherj.rest.client.UmapyoiService;
import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import com.kevinvg.umalauncherj.ui.UmaUiManager;
import com.kevinvg.umalauncherj.util.FileUtil;
import com.kevinvg.umalauncherj.util.Version;
import com.kevinvg.umalauncherj.util.Win32Util;
import io.quarkus.runtime.configuration.ConfigUtils;
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

@Slf4j
@Singleton
public class Updater {
    private final AppSettingsManager settings;
    private final UmaUiManager ui;
    private static final File UPDATE_FILE = FileUtil.getAppDataFile("update.tmp");
    private static final String ASSET_NAME = "UmaLauncherJ.exe";

    @RestClient
    UmapyoiService umapyoiService;

    private @Getter boolean done = false;
    private UpdateInfo updateInfo = null;

    @Inject
    Updater(AppSettingsManager settings, UmaUiManager ui) {
        this.settings = settings;
        this.ui = ui;
    }

    public void checkUpdate() {
        log.info("Checking for updates...");

        if (handleFromPreviousVersion()) {
            done = true;
            return;
        }

        if (isDevProfile()) {
            log.info("Skipping auto-update routine in dev profile.");
            done = true;
            return;
        }

        String skipVersion = settings.get(AppSettings.SettingKey.SKIP_VERSION);

        UpdateInfo latestVersion = determineLatestVersion();

        if (latestVersion == null) {
            log.info("Already on the latest version");
            done = true;
            return;
        }

        if (latestVersion.version().equals(new Version(skipVersion))) {
            log.info("User requested to skip update version {}", skipVersion);
            done = true;
            return;
        }

        askForUpdate(latestVersion);
    }

    private boolean isDevProfile() {
//        return ConfigUtils.getProfiles().contains("dev");
        return false;
    }

    private boolean handleFromPreviousVersion() {
        if (!UPDATE_FILE.exists()) {
            return false;
        }
        UPDATE_FILE.delete();

        var currentVersion = new Version(settings.<String>get(AppSettings.SettingKey.VERSION));
        ui.showInfoDialog("Update complete!", "Uma Launcher updated successfully to v%s".formatted(currentVersion.toString()));
        return true;
    }

    private UpdateInfo determineLatestVersion() {
        var releases = umapyoiService.getReleases();

        UpdateInfo latestVersion = null;
        for (var release : releases) {
            // TODO: Prerelease setting
            boolean isBeta = release.path("prerelease").asBoolean();

            String versionString = release.path("tag_name").asText("0.0.0");
            if (versionString.startsWith("v")) versionString = versionString.substring(1);

            Version version = new Version(versionString);
            if (version.equals(new Version())) {
                log.error("Failed to parse version tag!");
                return null;
            }

            String url = null;
            for (var asset : release.path("assets")) {
                if (!asset.path("name").asText().equals(ASSET_NAME)) {
                    continue;
                }
                url = asset.path("browser_download_url").asText();
            }

            if (url == null) {
                continue;
            }

            latestVersion = new UpdateInfo(version, isBeta, url, release.path("html_url").asText());
            break;
        }

        if (latestVersion == null) {
            return null;
        }

        var currentVersion = new Version(settings.<String>get(AppSettings.SettingKey.VERSION));
        if (latestVersion.version().compareTo(currentVersion) <= 0) {
            return null;
        }

        return latestVersion;
    }

    private void askForUpdate(UpdateInfo latestVersion) {
        ui.askForUpdate(latestVersion, this, settings);
    }

    public void askForUpdateCallback(UpdateInfo updateInfo, boolean success) {
        if (!success) {
            done = true;
            return;
        }
        this.updateInfo = updateInfo;
    }

    @Scheduled(every="1s", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    void performUpdate() {
        if (updateInfo == null) {
            return;
        }
        var curUpdate = updateInfo;
        updateInfo = null;

        ui.showUpdateDialog();

        log.info("Updating to version {}", curUpdate.version());
        log.info("Software is now softlocked");
        log.info("EXE_FOLDER {}", FileUtil.EXE_FOLDER);
        log.info("EXE_NAME {}", FileUtil.EXE_NAME);

        String exeFile = FileUtil.EXE_NAME;
        String exePath = FileUtil.EXE_FOLDER.resolve(exeFile).toAbsolutePath().toString();
        String withoutExt = exeFile.substring(0, exeFile.lastIndexOf('.'));
        String exePathWithoutExt = FileUtil.EXE_FOLDER.resolve(withoutExt).toAbsolutePath().toString();
        String oldFile = withoutExt + ".old";
        String oldPath = FileUtil.getAppDataFile(oldFile).getAbsolutePath();
        String tmpFile = withoutExt + ".tmp";
        String tmpPath = FileUtil.getAppDataFile(tmpFile).getAbsolutePath();

        log.info("Downloading new exe");
        try {
            var in = new URI(curUpdate.url()).toURL().openStream();
            Files.copy(in, Paths.get(tmpPath), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            log.error("Error downloading exe", e);
            ui.showErrorDialog("Error downloading exe");
            done = true;
            return;
        }

        log.info("Download complete. Performing switcheroo");
        try (PrintWriter writer = new PrintWriter(UPDATE_FILE)) {
            writer.write("");
        } catch (FileNotFoundException e) {
            log.error("Could not write update file");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("taskkill /F ");
        for (int pid : Win32Util.getAllProcessIds()) {
            sb.append("/PID ").append(pid).append(" ");
        }
        sb.append("& timeout /t 2 /nobreak && move /y \"%s\" \"%s\" && move /y \"%s\" \"%s\" && start /B \"\" \"%s\"".formatted(exePath, oldPath, tmpPath, exePath, exePathWithoutExt));

        String[] command = {"cmd", "/c", "start \"UmaLauncherUpdater\" \"cmd\" /c \"" + sb + "\""};
        log.info("Command: {}", Arrays.toString(command));
        try {
            Runtime.getRuntime().exec(command).waitFor();
        } catch (Exception e) {
            log.error("Error executing command", e);
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {}
        UPDATE_FILE.delete();
    }
}
