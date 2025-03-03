package com.kevinvg.umalauncherj.selenium;

import com.kevinvg.umalauncherj.selenium.browsers.Chrome;
import com.kevinvg.umalauncherj.selenium.browsers.Edge;
import com.kevinvg.umalauncherj.selenium.browsers.Firefox;

public enum BrowserType {
    FIREFOX(Firefox.class),
    CHROME(Chrome.class),
    EDGE(Edge.class),;

    public final Class<? extends Browser> driverClass;

    BrowserType(final Class<? extends Browser> driverClass) {
        this.driverClass = driverClass;
    }
}
