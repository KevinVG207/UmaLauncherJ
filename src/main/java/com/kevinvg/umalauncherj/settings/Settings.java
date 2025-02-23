package com.kevinvg.umalauncherj.settings;

import java.util.HashMap;
import java.util.Map;

public abstract class Settings {
    private final Map<String, Setting<?>> settings = new HashMap<>();
}
