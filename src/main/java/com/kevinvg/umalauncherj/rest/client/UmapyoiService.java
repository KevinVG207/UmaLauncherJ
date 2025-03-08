package com.kevinvg.umalauncherj.rest.client;

import com.kevinvg.umalauncherj.rest.client.domain.CharaNameData;
import com.kevinvg.umalauncherj.rest.client.domain.DiscordAsset;
import com.kevinvg.umalauncherj.rest.client.domain.OutfitData;
import com.kevinvg.umalauncherj.rest.client.domain.VpnData;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(baseUri = "https://umapyoi.net/api/v1")
public interface UmapyoiService {
    @GET
    @Path("/vpn/dmm")
    List<VpnData> getDmmVpns();

    @GET
    @Path("/vpn/cygames")
    List<VpnData> getCygamesVpns();

    @GET
    @Path("/uma-launcher/discord-assets")
    List<DiscordAsset> getDiscordAssets();

    @GET
    @Path("/character/names")
    List<CharaNameData> getCharacterNames();

    @GET
    @Path("/outfit")
    List<OutfitData> getOutfits();
}
