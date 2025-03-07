package com.kevinvg.umalauncherj.rest.client;

import com.kevinvg.umalauncherj.rest.client.domain.IpData;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("")
@RegisterRestClient(baseUri = "https://api.myip.com")
public interface IpService {
    @GET
    @Consumes(MediaType.TEXT_HTML)
    IpData getIp();
}
