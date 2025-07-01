package com.fabiogomesrocha.endpoint;

import com.fabiogomesrocha.model.User;
import com.fabiogomesrocha.service.JwtService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.json.JsonNumber;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import io.quarkus.security.identity.SecurityIdentity;

import java.util.Collections;
import java.util.Map;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    @Inject
    SecurityIdentity identity;
    @Inject
    JwtService jwtService;
    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/me")
    @RolesAllowed("admin")
    public Response getUserInfo() {
        return Response.ok(Map.of(
                "userId", jwt.getClaim("userId"),
                "user", jwt.getName(), // equivalente ao sub
                "roles", identity.getRoles()
        )).build();
    }


    @POST
    @Path("/login")
    @Transactional
    public Response login(User user) throws Exception {
        User dbUser = User.find("username", user.username).firstResult();

        if (dbUser != null && dbUser.password.equals(user.password)) {
            String token = jwtService.generateToken(dbUser.id, dbUser.username, dbUser.role);
            return Response.ok(Collections.singletonMap("token", token)).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }


}
