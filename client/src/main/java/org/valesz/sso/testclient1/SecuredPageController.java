package org.valesz.sso.testclient1;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecuredPageController {

    @RequestMapping(value = "/securedPage")
    public String securedPage(Model model) {
        model.addAttribute("authentication", SecurityContextHolder.getContext().getAuthentication());
        return "securedPage.html";
    }
}
