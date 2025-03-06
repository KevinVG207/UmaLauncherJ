package com.kevinvg.umalauncherj.selenium.instances;

import com.kevinvg.umalauncherj.selenium.Horsium;
import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import com.kevinvg.umalauncherj.ui.UmaUiManager;
import com.kevinvg.umalauncherj.util.Rect;
import io.quarkus.runtime.Shutdown;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

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
    @Shutdown
    protected void shutdown() {
        log.info("Quitting {}", getClass().getSimpleName());
        for (var driver : drivers) {
            for (int i = 0; i < 3; i++) {
                try {
                    driver.quit();
                    break;
                } catch (WebDriverException ignored) {
                }
            }
        }
        log.info("Finished quitting {}", getClass().getSimpleName());
    }

    @Override
    protected void setupPage() {
        Rect rect = settings.get(AppSettings.SettingKey.BROWSER_POSITION);

        if (rect != null && rect.isValid()) {
            driver.manage().window().setPosition(new org.openqa.selenium.Point(rect.getX(), rect.getY()));
            driver.manage().window().setSize(new org.openqa.selenium.Dimension(rect.getWidth(), rect.getHeight()));
        }


        executeScript("window.from_script = true;");

        // Setup lots of stuff
        executeScript("""
                if (window.UL_OVERLAY) {
                    window.UL_OVERLAY.remove();
                }
                window.UL_OVERLAY = document.createElement("div");
                window.GT_PAGE = document.getElementById("__next");
                window.OVERLAY_HEIGHT = "15rem";
                window.UL_OVERLAY.style.height = "max_content";
                window.UL_OVERLAY.style.width = "100%";
                window.UL_OVERLAY.style.padding = "0.5rem 0";
                window.UL_OVERLAY.style.position = "fixed";
                window.UL_OVERLAY.style.bottom = "100%";
                window.UL_OVERLAY.style.zIndex = 100;
                window.UL_OVERLAY.style.backgroundColor = "var(--c-bg-main)";
                window.UL_OVERLAY.style.borderBottom = "2px solid var(--c-topnav)";
                
                var ul_data = document.createElement("div");
                ul_data.id = "ul-data";
                window.UL_OVERLAY.appendChild(ul_data);
                
                window.UL_OVERLAY.ul_data = ul_data;
                
                ul_data.style.display = "flex";
                ul_data.style.alignItems = "center";
                ul_data.style.justifyContent = "center";
                ul_data.style.flexDirection = "column";
                ul_data.style.gap = "0.5rem";
                ul_data.style.fontSize = "0.9rem";
                
                var ul_dropdown = document.createElement("div");
                ul_dropdown.id = "ul-dropdown";
                ul_dropdown.classList.add("ul-overlay-button");
                ul_dropdown.style = "position: fixed;right: 0;top: 0;width: 3rem;height: 1.6rem;background-color: var(--c-bg-main);text-align: center;z-index: 101;line-height: 1.5rem;border-left: 2px solid var(--c-topnav);border-bottom: 2px solid var(--c-topnav);border-bottom-left-radius: 0.5rem;cursor: pointer;";
                ul_dropdown.textContent = "⯅";
                window.UL_OVERLAY.appendChild(ul_dropdown);
                
                var ul_skills = document.createElement("div");
                ul_skills.id = "ul-skills";
                ul_skills.classList.add("ul-overlay-button");
                ul_skills.style = "position: fixed; right: 50px; top: 0; width: 3.5rem; height: 1.6rem; background-color: var(--c-bg-main); text-align: center; z-index: 101; line-height: 1.5rem; border-left: 2px solid var(--c-topnav); border-bottom: 2px solid var(--c-topnav); border-right: 2px solid var(--c-topnav); border-bottom-left-radius: 0.5rem; border-bottom-right-radius: 0.5rem; cursor: pointer; transition: top 0.5s ease 0s;";
                ul_skills.textContent = "Skills";
                window.UL_OVERLAY.appendChild(ul_skills);
                
                window.hide_overlay = function() {
                    window.UL_DATA.expanded = false;
                    document.getElementById("ul-dropdown").textContent = "⯆";
                    // document.getElementById("ul-dropdown").style.top = "-2px";
                    [...document.querySelectorAll(".ul-overlay-button")].forEach(div => {
                        div.style.top = "-2px";
                    })
                    window.GT_PAGE.style.paddingTop = "0";
                    window.UL_OVERLAY.style.bottom = "100%";
                }
                
                window.expand_overlay = function() {
                    window.UL_DATA.expanded = true;
                
                    var height = window.UL_OVERLAY.offsetHeight;
                    console.log(height)
                    window.OVERLAY_HEIGHT = height + "px";
                
                    document.getElementById("ul-dropdown").textContent = "⯅";
                    // document.getElementById("ul-dropdown").style.top = "calc(" + window.OVERLAY_HEIGHT + " - 2px)";
                    [...document.querySelectorAll(".ul-overlay-button")].forEach(div => {
                        div.style.top = "calc(" + window.OVERLAY_HEIGHT + " - 2px)";
                    })
                    window.GT_PAGE.style.paddingTop = window.OVERLAY_HEIGHT;
                    window.UL_OVERLAY.style.bottom = "calc(100% - " + window.OVERLAY_HEIGHT + ")";
                }
                
                ul_dropdown.addEventListener("click", function() {
                    if (window.UL_DATA.expanded) {
                        window.hide_overlay();
                    } else {
                        window.expand_overlay();
                    }
                });
                
                window.UL_DATA = {
                    overlay_html: "",
                    expanded: true
                };
                
                document.body.prepend(window.UL_OVERLAY);
                
                window.UL_OVERLAY.querySelector("#ul-dropdown").style.transition = "top 0.5s";
                window.UL_OVERLAY.style.transition = "bottom 0.5s";
                window.GT_PAGE.style.transition = "padding-top 0.5s";
                
                window.update_overlay = function() {
                    window.UL_OVERLAY.ul_data.replaceChildren();
                    window.UL_OVERLAY.ul_data.insertAdjacentHTML("afterbegin", window.UL_DATA.overlay_html)
                    //window.UL_OVERLAY.ul_data.innerHTML = window.UL_DATA.overlay_html;
                
                    if (window.UL_DATA.expanded) {
                        window.expand_overlay();
                        //setTimeout(window.expand_overlay, 100);
                    }
                };
                
                // Skill window.
                window.await_skill_window_timeout = null;
                window.await_skill_window = function() {
                    window.await_skill_window_timeout = setTimeout(function() {
                        ul_skills.style.filter = "";
                    }, 15000);
                
                    ul_skills.style.filter = "brightness(0.5)";
                    fetch('http://127.0.0.1:3150/open-skill-window', { method: 'POST' });
                }
                window.skill_window_opened = function() {
                    if (window.await_skill_window_timeout) {
                        clearTimeout(window.await_skill_window_timeout);
                    }
                    ul_skills.style.filter = "";
                }
                
                ul_skills.addEventListener("click", window.await_skill_window);
                """);

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

    public void updateOverlay(String html) {
        if (html == null) {
            return;
        }

        executeScript("""
                window.UL_DATA.overlay_html = arguments[0];
                window.update_overlay();"""
        , html);
    }
}
