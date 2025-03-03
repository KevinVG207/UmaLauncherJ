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

@Slf4j
public class Chrome extends Browser {
    @Override
    protected WebDriver setup(AppSettingsManager settings, String url) {
        var service = ChromeDriverService.createDefaultService();
        var options = new ChromeOptions();
        options.addArguments(
                "--user-data-dir=" + ProfileManager.chromeProfileFile.toPath(),
                "--remote-debugging-port=9222",
                "--new-window",
                "-app=" + url
        );

        if (settings.get(AppSettings.SettingKey.ENABLE_BROWSER_OVERRIDE)) {
            String driverPath = settings.get(AppSettings.SettingKey.BROWSER_CUSTOM_DRIVER);
            if (driverPath != null && !driverPath.isEmpty()) {
                service.setExecutable(driverPath);
            }

            String binaryPath = settings.get(AppSettings.SettingKey.BROWSER_CUSTOM_BINARY);
            if (binaryPath != null && !binaryPath.isEmpty()) {
                options.setBinary(binaryPath);
            }
        }

        log.info("Chrome driver path: {}", service.getExecutable());

        return new ChromeDriver(service, options);
    }
}
