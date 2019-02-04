package org.valesz.ssopoc.server.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * Endpoint for revoking OAuth2 tokens (logout).
 *
 * Based on: https://www.baeldung.com/logout-spring-security-oauth
 */
@Controller
public class RevokeTokenEndpoint {

    public RevokeTokenEndpoint() {
        System.out.println("Hello!");
    }

    @Autowired
    private ConsumerTokenServices tokenServices;

    @RequestMapping(method = RequestMethod.DELETE, value = "/oauth/revoke-token")
    @ResponseStatus(HttpStatus.OK)
    public void revokeToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.contains("Bearer")){
            String tokenId = authorization.substring("Bearer".length()+1);
            tokenServices.revokeToken(tokenId);
            System.out.println("Token "+tokenId+" revoked");
        } else {
            System.out.println("No authorization header or it doesn't contain 'Bearer' value");
        }
    }

    /**
     * Used for pinging, should be removed in prod.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/oauth/revoke-token")
    @ResponseStatus(HttpStatus.OK)
    public void revokeTokenAlive() {

    }
}
