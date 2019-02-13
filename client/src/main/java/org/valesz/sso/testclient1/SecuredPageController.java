package org.valesz.sso.testclient1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecuredPageController {

    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;

    @Autowired
    private OAuth2ProtectedResourceDetails resourceDetails;

    @RequestMapping(value = "/dataPage")
    public String dataPage(Model model) {
        String dataUrl = "http://myoauth2:8081/api/v1/user/me/data";
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails, oAuth2ClientContext);
        ResponseEntity<DataVO> responseEntity = oAuth2RestTemplate.getForEntity(dataUrl, DataVO.class);
        DataVO data;
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            data = responseEntity.getBody();
        } else {
            data = new DataVO();
            data.setName("error");
            data.setValue1(responseEntity.getStatusCode().value());
        }
        model.addAttribute("data", data);

        return "dataPage.html";
    }

    @RequestMapping(value = "/securedPage")
    public String securedPage(Model model) {
        model.addAttribute("authentication", SecurityContextHolder.getContext().getAuthentication());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("token", ((OAuth2AuthenticationDetails)auth.getDetails()).getTokenValue());
        return "securedPage.html";
    }

    /**
     * Handles logout:
     * 1. invalidates token and clears security context.
     * 2. redirects to auth server's logout page where user confirms logout
     * 3. auth server will then revoke all tokens connected to logged user and destroys his session
     *
     * @return
     */
    @RequestMapping(value = "/logmeout")
    public String logmeout() {

        oAuth2ClientContext.setAccessToken(null);
        SecurityContextHolder.clearContext();
        System.out.println("Context cleared, access token set to null");

        return "redirect:http://localhost:8081/signout";
    }
}
