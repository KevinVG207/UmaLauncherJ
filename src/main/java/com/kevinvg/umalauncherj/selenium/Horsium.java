package com.kevinvg.umalauncherj.selenium;

import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import com.kevinvg.umalauncherj.util.Rect;
import com.kevinvg.umalauncherj.ui.UmaUiManager;
import io.quarkus.runtime.Shutdown;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Singleton
public class Horsium {
    private final AppSettingsManager settings;
    private final UmaUiManager ui;
    private final List<WebDriver> drivers = new ArrayList<>();
    private WebDriver driver;
    private String activeHandle = "";
    @Setter
    private String gameToraUrl = "";

    @Inject
    Horsium(AppSettingsManager settings, UmaUiManager ui) {
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
            selectedBrowser = "Auto";
            settings.set(AppSettings.SettingKey.SELECTED_BROWSER, selectedBrowser);
        }

        if (selectedBrowser.equals("Auto")) {
            attemptList.addAll(Arrays.asList(BrowserType.values()));
        }

        WebDriver newDriver = null;
        for (BrowserType browserType : attemptList) {
            try {
                newDriver = browserType.driverClass.getConstructor().newInstance().setup(settings, gameToraUrl);
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
        newDriver.get(gameToraUrl);
        this.activeHandle = newDriver.getWindowHandle();
        setupGameToraPage();
    }

    public Object executeScript(String script, Object... args) {
        if (!driverIsAlive()) {
            return null;
        }
        if (!(driver instanceof JavascriptExecutor executor)) {
            log.error("Browser driver is not JavascriptExecutor!");
            return null;
        }

        return executor.executeScript(script, args);
    }

    public boolean executeBooleanScript(String script, Object... args) {
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

        driver.switchTo().window(activeHandle);
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

    @Shutdown
    void shutdown() {
        log.info("Quitting webdrivers");
        drivers.forEach(WebDriver::quit);
        log.info("Webdrivers quit");
    }

    private void setupGameToraPage() {
        Rect rect = settings.get(AppSettings.SettingKey.BROWSER_POSITION);

        if (rect != null) {
            driver.manage().window().setPosition(new org.openqa.selenium.Point(rect.getX(), rect.getY()));
            driver.manage().window().setSize(new org.openqa.selenium.Dimension(rect.getWidth(), rect.getHeight()));
        }


        executeScript("window.from_script = true;");

        // Setup window position requests
        executeScript("""
                window.send_screen_rect = function() {
                    let rect = {
                        'x': window.screenX,
                        'y': window.screenY,
                        'width': window.outerWidth,
                        'height': window.outerHeight
                    };
                    fetch('http://127.0.0.1:3150/helper-window-rect', { method: 'POST', body: JSON.stringify(rect), headers: { 'Content-Type': 'application/json' } });
                    setTimeout(window.send_screen_rect, 2000);
                }
                setTimeout(window.send_screen_rect, 2000);""");

        // Hide filters
        executeScript("""
                document.querySelector("[class^='filters_filter_container_']").style.display = "none";""");

        // Handle dark mode
        executeScript("""
                document.querySelector("[class^='styles_header_settings_']").click()""");
        waitForTrue("""
                return document.querySelector("[class^='filters_toggle_button_']") != null;""");

        boolean darkEnabled = executeBooleanScript("""
                return document.querySelector("[class^='tooltips_tooltip_']").querySelector("[class^='filters_toggle_button_']").childNodes[0].querySelector("input").checked;""");
        if (darkEnabled != settings.<Boolean>get(AppSettings.SettingKey.GAMETORA_DARK_MODE)) {
            executeScript("""
                    document.querySelector("[class^='tooltips_tooltip_']").querySelector("[class^='filters_toggle_button_']").childNodes[0].querySelector("input").click()""");
        }
        executeScript("""
                document.querySelector("[class^='styles_header_settings_']").click()""");

        // Enable all cards
        executeScript("""
                document.querySelector("[class^='filters_settings_button_']").click()""");
        boolean allCardsEnabled = executeBooleanScript("""
                return document.getElementById("allAtOnceCheckbox").checked;""");
        if (!allCardsEnabled) {
            executeScript("""
                    document.getElementById("allAtOnceCheckbox").click()""");
        }
        executeScript("""
                document.querySelector("[class^='filters_confirm_button_']").click()""");

        // Remove cookies banner
        waitForTrue("""
                return document.getElementById("adnote") != null;""");
        executeScript("""
                document.getElementById("adnote").style.display = 'none';""");
    }

    private void waitForTrue(String script, Object... args) {
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

    public void scrollToEventTitles(List<String> titles) {
        var eventElement = determineEventElement(titles);

        if (eventElement == null) {
            log.warn("No event element found on page based on titles: {}", titles);
            return;
        }

        executeScript("""
                if (arguments[0]) {
                    arguments[0].click();
                    window.scrollBy({top: arguments[0].getBoundingClientRect().bottom - window.innerHeight + 32, left: 0, behavior: 'smooth'});
                }""", eventElement);
    }

    public void closeEventResultPopups() {
        executeScript("""
                document.querySelectorAll("[class^='compatibility_viewer_item_'][aria-expanded=true]").forEach(e => e.click());""");
    }

    @SuppressWarnings("unchecked")
    private Object determineEventElement(List<String> titles) {
        List<List<Object>> rankedElements = new ArrayList<>();
        try {
            for (var title : titles) {
                List<List<Object>> possibleElements = (List<List<Object>>) executeScript("""
                    let a = document.querySelectorAll("[class^='compatibility_viewer_item_']");
                    let ele = [];
                    for (let i = 0; i < a.length; i++) {
                        let item = a[i];
                        if (item.textContent.includes(arguments[0])) {
                            let diff = item.textContent.length - arguments[0].length;
                            ele.push([diff, item, item.textContent]);
                        }
                    }
                    return ele;""", title);

                if (possibleElements == null || possibleElements.isEmpty()) {
                    continue;
                }

                possibleElements.sort((a, b) -> {
                    int c = (int) a.getFirst();
                    int d = (int) a.getLast();
                    return c - d;
                });

                rankedElements.addAll(possibleElements);
            }

            if (rankedElements.isEmpty()) {
                return null;
            }

            rankedElements.sort((a, b) -> {
                int c = (int) a.getFirst();
                int d = (int) a.getLast();
                return c - d;
            });

            var out = rankedElements.getFirst();
            log.info("Event element: {}", out.get(2));
            return out.get(1);
        } catch (Exception e) {
            log.error("Error determining event element", e);
            return null;
        }
    }

    public void selectExtraSupportCard(int supportCardId) {
        log.info("Selecting support card: {}", supportCardId);

        executeScript("""
                document.getElementById("boxSupportExtra").click();""");

        waitForTrue("""
                return document.getElementById("30021") != null;""");

        executeScript("""
            
            
            var cont = document.getElementById("30021").parentElement.parentElement;
            
            var ele = document.getElementById(arguments[0].toString());

            if (ele) {
                ele.click();
                return;
            }
            cont.querySelector('img[src="/images/ui/close.png"]').click();"""
        , supportCardId);
    }
}
