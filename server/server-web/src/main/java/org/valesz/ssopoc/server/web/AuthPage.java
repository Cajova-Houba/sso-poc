package org.valesz.ssopoc.server.web;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.web.bind.support.SimpleSessionStatus;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

public class AuthPage extends WebPage {

    @SpringBean
    private AuthorizationEndpoint authorizationEndpoint;

    public AuthPage(PageParameters parameters) {
        super(parameters);

        Injector.get().inject(this);

        final HttpServletRequest request = (HttpServletRequest)getRequest();
        if(request.getMethod().equalsIgnoreCase("GET")) {
            callAuthEndpoint(parameters);
        } else {
            // todo:
        }
    }

    private void callAuthEndpoint(PageParameters pageParameters) {
        Map<String,String> params = new HashMap<>();
        for(String key : pageParameters.getNamedKeys()) {
            System.out.println(key+": "+pageParameters.get(key).toString());
            params.put(key, pageParameters.get(key).toString());
        }

        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> model = new HashMap<>();
        authorizationEndpoint.authorize(model, params, new SimpleSessionStatus(), principal);

        // now, call approveOrDeny and return token
//        authorizationEndpoint.approveOrDeny()
    }

}
