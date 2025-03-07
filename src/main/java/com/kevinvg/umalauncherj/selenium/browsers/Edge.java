package com.kevinvg.umalauncherj.selenium.browsers;

import com.kevinvg.umalauncherj.selenium.Browser;
import com.kevinvg.umalauncherj.selenium.ProfileManager;
import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;

@Slf4j
public class Edge extends Browser {
    @Override
    protected WebDriver setup(AppSettingsManager settings, String url) {
        var service = EdgeDriverService.createDefaultService();
        var options = new EdgeOptions();
        options.addArguments(
                "--user-data-dir=" + ProfileManager.chromeProfileFile.toPath(),
                "--remote-debugging-port=9222",
                "--new-window",
                "-app=" + url
        );

        if (Boolean.TRUE.equals(settings.<Boolean>get(AppSettings.SettingKey.ENABLE_BROWSER_OVERRIDE))) {
            String driverPath = settings.get(AppSettings.SettingKey.BROWSER_CUSTOM_DRIVER);
            if (driverPath != null && !driverPath.isEmpty()) {
                service.setExecutable(driverPath);
            }

            String binaryPath = settings.get(AppSettings.SettingKey.BROWSER_CUSTOM_BINARY);
            if (binaryPath != null && !binaryPath.isEmpty()) {
                options.setBinary(binaryPath);
            }
        }

        log.info("Edge driver path: {}", service.getExecutable());

        return new EdgeDriver(service, options);
    }
}
