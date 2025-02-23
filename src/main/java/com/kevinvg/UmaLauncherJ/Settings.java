package com.kevinvg.UmaLauncherJ;

import org.springframework.stereotype.Component;

@Component
public class Settings {
    public String getSetting(String settingKey) {
        System.out.println("Setting asked: " + settingKey);
        return settingKey;
    }
}
