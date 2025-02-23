package com.kevinvg.umalauncherj;

import java.util.Map;

public abstract class MapSerializable {
    public abstract Map<String, ?> toMap();
    public abstract void fromMap(Map<String, ?> map);
}
