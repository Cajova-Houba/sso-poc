package org.valesz.ssopoc.server.rest.impl;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.valesz.ssopoc.server.rest.UserService;

import javax.ws.rs.core.Response;

public class UserServiceImpl implements UserService {

    @Override
    public Response me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) {
            throw new InsufficientAuthenticationException("There is no client authentication. Try adding an appropriate authentication filter.");
        }

        return Response.ok(auth.getPrincipal()).build();
    }
}
