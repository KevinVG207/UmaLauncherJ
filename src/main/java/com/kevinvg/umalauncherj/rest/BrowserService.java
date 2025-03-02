package com.kevinvg.umalauncherj.rest;

import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import com.kevinvg.umalauncherj.util.Rect;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
public class BrowserService {
    AppSettingsManager settings;

    @Inject
    BrowserService(AppSettingsManager settings) {
        this.settings = settings;
    }

    @Path("helper-window-rect")
    @POST
    public Response helperWindowRect(Rect rect) {
        settings.set(AppSettings.SettingKey.BROWSER_POSITION, rect);
        return Response.ok().build();
    }
}
