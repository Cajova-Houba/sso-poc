package org.valesz.ssopoc.server.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;

@Produces(MediaType.APPLICATION_JSON)
public interface UserService extends Serializable {

    /**
     * Returns info about currently logged user. If no user is logged in, method throws an exception.
     *
     * @return Response containing Principal object with user's data.
     */
    @Path("/user/me")
    @GET
    Response me();

    /**
     * Returns object representing user's data. Actual values in data object are not relevant.
     *
     * @return
     */
    @Path("/user/me/data")
    @GET
    Response myData();
}
