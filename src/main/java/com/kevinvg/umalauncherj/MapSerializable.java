package com.kevinvg.umalauncherj;

import java.util.Map;

public interface MapSerializable {
    Map<String, ?> toMap();
    void fromMap(Map<String, ?> map);
}
