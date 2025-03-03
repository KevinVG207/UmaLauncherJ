package com.kevinvg.umalauncherj.selenium;

import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import jakarta.inject.Inject;
import org.openqa.selenium.WebDriver;

public abstract class Browser {
    protected abstract WebDriver setup(AppSettingsManager settings, String url);
}
