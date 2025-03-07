package com.kevinvg.umalauncherj.selenium.browsers;

import com.kevinvg.umalauncherj.selenium.Browser;
import com.kevinvg.umalauncherj.selenium.ProfileManager;
import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.GeckoDriverService;

@Slf4j
public class Firefox extends Browser {
    @Override
    protected WebDriver setup(AppSettingsManager settings, String url) {
        var service = GeckoDriverService.createDefaultService();
        var profile = new FirefoxProfile(ProfileManager.firefoxProfileFile);
        var options = new FirefoxOptions();
        options.setProfile(profile);

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

        log.info("Firefox driver path: {}", service.getExecutable());
        log.info("Firefox binary path: {}", options.getBinary());

        return new FirefoxDriver(service, options);
    }
}
