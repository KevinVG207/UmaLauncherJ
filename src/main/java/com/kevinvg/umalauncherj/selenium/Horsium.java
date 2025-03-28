package com.kevinvg.umalauncherj.selenium;

import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import com.kevinvg.umalauncherj.ui.UmaUiManager;
import io.quarkus.runtime.Shutdown;
import jakarta.inject.Inject;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public abstract class Horsium {
    protected final AppSettingsManager settings;
    protected final UmaUiManager ui;
    protected final List<WebDriver> drivers = new ArrayList<>();
    protected WebDriver driver;
    private String activeHandle = "";
    @Setter
    private String url = "";

    @Inject
    protected Horsium(AppSettingsManager settings, UmaUiManager ui) {
        this.settings = settings;
        this.ui = ui;
    }

    public void setupBrowser() {
        String selectedBrowser = settings.<String>get(AppSettings.SettingKey.SELECTED_BROWSER).toUpperCase();
        List<BrowserType> attemptList = new ArrayList<>();

        try {
            attemptList.add(BrowserType.valueOf(selectedBrowser));
        } catch (IllegalArgumentException e) {
            log.error("Invalid browser selected: {}", selectedBrowser);
            selectedBrowser = "AUTO";
            settings.set(AppSettings.SettingKey.SELECTED_BROWSER, "AUTO");
        }

        if (selectedBrowser.equals("AUTO")) {
            attemptList.addAll(Arrays.asList(BrowserType.values()));
        }

        WebDriver newDriver = null;
        for (BrowserType browserType : attemptList) {
            try {
                newDriver = browserType.driverClass.getConstructor().newInstance().setup(settings, url);
                break;
            } catch (Exception e) {
            }
        }

        if (newDriver == null) {
            ui.showErrorDialog("Unable to start any browser.<br>Make sure you have one of the supported browsers installed<br>or set a webdriver and binary path in the preferences.");
            return;
        }

        drivers.add(newDriver);
        this.driver = newDriver;
        log.info("Browsing to {}", url);
        newDriver.get(url);
        this.activeHandle = newDriver.getWindowHandle();
        this.setupPage();
    }

    public Object executeScript(String script, Object... args) {
        ensureTabOpen();

        if (!(driver instanceof JavascriptExecutor executor)) {
            log.error("Browser driver is not JavascriptExecutor!");
            return null;
        }

        return executor.executeScript(script, args);
    }

    protected boolean executeBooleanScript(String script, Object... args) {
        var res = executeScript(script, args);
        return res instanceof Boolean ok && ok;
    }

    public void ensureTabOpen() {
        if (!driverIsAlive()) {
            setupBrowser();
            if (!driverIsAlive()) {
                // Failed to start.
                return;
            }
        }

        if (!driver.getWindowHandle().equals(activeHandle)) {
            driver.switchTo().window(activeHandle);
        }
    }

    public boolean driverIsAlive() {
        if (driver == null) {
            return false;
        }
        try {
            if (driver.getWindowHandles().contains(activeHandle)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    protected abstract void shutdown();

    protected abstract void setupPage();

    protected void waitForTrue(String script, Object... args) {
        while (true) {
            if (executeBooleanScript(script, args)) {
                return;
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void close() {
        if (driver == null) {
            return;
        }
        driver.quit();
    }
}
