package org.valesz.ssopoc.server.auth.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Map;

//@Controller
public class CustomTokenEndpoint {

    @Autowired
    private TokenEndpoint tokenEndpoint;

//    @RequestMapping("/oauth2/token")
    public ResponseEntity<OAuth2AccessToken> getAccessToken(Principal principal, @RequestParam
    Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return tokenEndpoint.getAccessToken(principal, parameters);
    }

//    @RequestMapping(value = "/oauth2/token", method= RequestMethod.POST)
    public ResponseEntity<OAuth2AccessToken> postAccessToken(Principal principal, @RequestParam
            Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return tokenEndpoint.postAccessToken(principal, parameters);
    }
}
