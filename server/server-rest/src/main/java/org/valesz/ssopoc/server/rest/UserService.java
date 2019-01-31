package org.valesz.ssopoc.server.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;

@Path("/user/me")
@Produces(MediaType.APPLICATION_JSON)
public interface UserService extends Serializable {

    /**
     * Returns info about currently logged user. If no user is logged in, method throws an exception.
     *
     * @return Response containing Principal object with user's data.
     */
    @GET
    Response me();
}
