package com.kevinvg.umalauncherj.settings;

import com.kevinvg.umalauncherj.settings.types.ListSetting;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public abstract class Settings<T extends Enum<?>> {
    protected final Map<T, Setting<?>> settings = new HashMap<>();
}
