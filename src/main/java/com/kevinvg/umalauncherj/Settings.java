package com.kevinvg.umalauncherj;

import jakarta.inject.Singleton;

@Singleton
public class Settings {
    public String getSetting(String settingKey) {
        System.out.println("Setting asked: " + settingKey);
        return settingKey;
    }
}
