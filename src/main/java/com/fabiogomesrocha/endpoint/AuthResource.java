package com.fabiogomesrocha.endpoint;

import com.fabiogomesrocha.model.User;
import com.fabiogomesrocha.service.JwtService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import java.util.Collections;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    JwtService jwtService;

    @POST
    @Path("/login")
    @Transactional
    public Response login(User user) throws Exception {
        User dbUser = User.find("username", user.username).firstResult();

        if (dbUser != null && dbUser.password.equals(user.password)) {
            String token = jwtService.generateToken(dbUser.username, dbUser.role);
            return Response.ok(Collections.singletonMap("token", token)).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
