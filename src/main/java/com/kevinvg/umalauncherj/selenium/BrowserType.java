package com.kevinvg.umalauncherj.selenium;

import com.kevinvg.umalauncherj.selenium.browsers.Firefox;
import org.openqa.selenium.WebDriver;

public enum BrowserType {
    FIREFOX(Firefox.class);

    public final Class<? extends Browser> driverClass;

    BrowserType(final Class<? extends Browser> driverClass) {
        this.driverClass = driverClass;
    }
}
