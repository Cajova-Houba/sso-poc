package org.valesz.ssopoc.server.web;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.Collection;

public class LogoutPage extends WebPage {

    @SpringBean
    private TokenStore tokenStore;

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(createLogoutForm("logoutForm"));
    }


    private Form createLogoutForm(String markupId) {
        return new Form(markupId) {
            @Override
            protected void onSubmit() {
                // perform logout and revoke all token connected to user

                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth == null) {
                    System.out.println("Authentication is null");
                    throw new RestartResponseException(HomePage.class);
                } else {
                    String username = auth.getName();
                    Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName("SampleClientId", username);
                    System.out.println(tokens.size()+" tokens found for user '"+username+"'");
                    tokens.forEach(token -> {
                        if (token.getRefreshToken() != null) {
                            tokenStore.removeRefreshToken(token.getRefreshToken());
                        }
                        tokenStore.removeAccessToken(token);
                    });
                    SecurityContextHolder.getContext().setAuthentication(null);
                    SecurityContextHolder.clearContext();

                    System.out.println("Redirecting to home.");
                    throw new RestartResponseException(HomePage.class);
                }
            }
        };
    }
}
