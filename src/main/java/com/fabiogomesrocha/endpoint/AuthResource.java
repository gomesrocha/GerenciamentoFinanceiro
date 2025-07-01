package com.fabiogomesrocha.endpoint;

import com.fabiogomesrocha.dto.LoginDTO;
import com.fabiogomesrocha.dto.UserInfoDTO;
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
        UserInfoDTO userInfo = new UserInfoDTO(
                jwt.getClaim("userId"),
                jwt.getName(),
                identity.getRoles()
        );
        return Response.ok(userInfo).build();
    }



    @POST
    @Path("/login")
    @Transactional
    public Response login(LoginDTO loginDTO) throws Exception {
        User dbUser = User.find("username", loginDTO.username).firstResult();

        if (dbUser != null && dbUser.password.equals(loginDTO.password)) {
            String token = jwtService.generateToken(dbUser.id, dbUser.username, dbUser.role);
            return Response.ok(Map.of("token", token)).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }


}
