package com.kevinvg.umalauncherj.selenium.instances;

import com.kevinvg.umalauncherj.selenium.Horsium;
import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import com.kevinvg.umalauncherj.ui.UmaUiManager;
import com.kevinvg.umalauncherj.util.Rect;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Singleton
public class GtEventHelper extends Horsium {
    @Inject
    GtEventHelper(AppSettingsManager settings, UmaUiManager ui) {
        super(settings, ui);
    }

    @Override
    protected void setupPage() {
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
        executeScript("document.querySelector(\"[class^='filters_filter_container_']\").style.display = \"none\";");

        // Handle dark mode
        executeScript("document.querySelector(\"[class^='styles_header_settings_']\").click()");
        waitForTrue("return document.querySelector(\"[class^='filters_toggle_button_']\") != null;");

        boolean darkEnabled = executeBooleanScript("return document.querySelector(\"[class^='tooltips_tooltip_']\").querySelector(\"[class^='filters_toggle_button_']\").childNodes[0].querySelector(\"input\").checked;");
        if (darkEnabled != settings.<Boolean>get(AppSettings.SettingKey.GAMETORA_DARK_MODE)) {
            executeScript("document.querySelector(\"[class^='tooltips_tooltip_']\").querySelector(\"[class^='filters_toggle_button_']\").childNodes[0].querySelector(\"input\").click()");
        }
        executeScript("document.querySelector(\"[class^='styles_header_settings_']\").click()");

        // Enable all cards
        executeScript("document.querySelector(\"[class^='filters_settings_button_']\").click()");
        boolean allCardsEnabled = executeBooleanScript("return document.getElementById(\"allAtOnceCheckbox\").checked;");
        if (!allCardsEnabled) {
            executeScript("document.getElementById(\"allAtOnceCheckbox\").click()");
        }
        executeScript("document.querySelector(\"[class^='filters_confirm_button_']\").click()");

        // Remove cookies banner
        waitForTrue("return document.getElementById(\"adnote\") != null;");
        executeScript("document.getElementById(\"adnote\").style.display = 'none';");
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
        if (!driverIsAlive()) {
            return;
        }

        executeScript("document.querySelectorAll(\"[class^='compatibility_viewer_item_'][aria-expanded=true]\").forEach(e => e.click());");
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

        executeScript("document.getElementById(\"boxSupportExtra\").click();");

        waitForTrue("return document.getElementById(\"30021\") != null;");

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
