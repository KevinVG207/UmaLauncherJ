package com.kevinvg.umalauncherj.rest.client;

import com.kevinvg.umalauncherj.rest.client.domain.IpDataBackup;
import io.quarkus.rest.client.reactive.ClientQueryParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("")
@RegisterRestClient(baseUri = "https://api.ipify.org/?format=json")
public interface IpServiceBackup {
    @GET
    IpDataBackup getIp();
}
