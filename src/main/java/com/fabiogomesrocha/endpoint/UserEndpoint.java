package com.fabiogomesrocha.endpoint;

import com.fabiogomesrocha.dto.UserDTO;
import com.fabiogomesrocha.model.User;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserEndpoint {

    @GET
    public List<User> listAll() {
        return User.listAll();
    }

    @GET
    @Path("/{id}")
    public User findById(@PathParam("id") Long id) {
        return User.findById(id);
    }

    @POST
    @Transactional
    public User create(UserDTO dto) {
        User user = new User();
        user.username = dto.username;
        user.password = dto.password; // ⚠️ idealmente use hash
        user.role = dto.role;
        user.persist();
        return user;
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public User update(@PathParam("id") Long id, UserDTO dto) {
        User user = User.findById(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        user.username = dto.username;
        user.password = dto.password;
        user.role = dto.role;
        return user;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        User.deleteById(id);
    }
}
