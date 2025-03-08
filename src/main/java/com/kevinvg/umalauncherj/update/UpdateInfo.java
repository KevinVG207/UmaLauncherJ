package com.kevinvg.umalauncherj.update;

import com.kevinvg.umalauncherj.util.Version;

public record UpdateInfo(Version version, boolean beta, String url, String releaseNotesUrl) {
}
