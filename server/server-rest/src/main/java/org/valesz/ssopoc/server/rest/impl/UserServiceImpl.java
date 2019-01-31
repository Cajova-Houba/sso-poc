package org.valesz.ssopoc.server.rest.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.valesz.ssopoc.server.rest.UserService;

import javax.security.auth.Subject;
import javax.ws.rs.core.Response;
import java.security.Principal;

public class UserServiceImpl implements UserService {

    @Override
    public Response me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String tmp = auth == null ? "[no auth]" : "["+auth.getName()+"]";

        return Response.ok(new Principal() {
            @Override
            public boolean implies(Subject subject) {
                return true;
            }

            @Override
            public String getName() {
                return tmp;
            }
        }).build();
    }
}
