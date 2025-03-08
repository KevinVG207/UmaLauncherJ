package com.kevinvg.umalauncherj.rest;

import com.kevinvg.umalauncherj.settings.app.AppSettings;
import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import com.kevinvg.umalauncherj.util.Rect;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("helper-window-rect")
public class BrowserService {
    AppSettingsManager settings;

    @Inject
    BrowserService(AppSettingsManager settings) {
        this.settings = settings;
    }

    @POST
    public Response helperWindowRect(Rect rect) {
        var existingRect = settings.<Rect>get(AppSettings.SettingKey.BROWSER_POSITION);

        if (rect.equals(existingRect)) {
            return Response.ok().build();
        }

        settings.set(AppSettings.SettingKey.BROWSER_POSITION, rect);
        return Response.ok().build();
    }
}
