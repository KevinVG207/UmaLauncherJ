package com.kevinvg.umalauncherj.update;

import com.kevinvg.umalauncherj.rest.client.UmapyoiService;
import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import com.kevinvg.umalauncherj.ui.UmaUiManager;
import com.kevinvg.umalauncherj.util.FileUtil;
import com.kevinvg.umalauncherj.util.Version;
import io.quarkus.runtime.configuration.ConfigUtils;
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.File;

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
        handleFromPreviousVersion();

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
        return false;
//        return ConfigUtils.getProfiles().contains("dev");
    }

    private void handleFromPreviousVersion() {
        if (!UPDATE_FILE.exists()) {
            return;
        }
        UPDATE_FILE.delete();

        var currentVersion = new Version(settings.<String>get(AppSettings.SettingKey.VERSION));
        ui.showInfoDialog("Update complete!", "Uma Launcher updated successfully to v%s".formatted(currentVersion.toString()));
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

        log.info("Updating to version {}", curUpdate.version());
        log.info("Software is now softlocked");
    }
}
