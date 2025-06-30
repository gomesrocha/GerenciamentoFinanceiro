package com.fabiogomesrocha.endpoint;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.util.Map;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("")
public class RootEndpoint {
    @GET
    @Produces(APPLICATION_JSON)
    public Map<String, String> get(){
        return Map.of("message", "Welcome to our Gerenciamento Financeiro API!");
    }
}
