package org.valesz.ssopoc.server.web;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	public HomePage(final PageParameters parameters) {
		super(parameters);

		add(new Label("version", getApplication().getFrameworkSettings().getVersion()));

		// TODO Add your page's components here
		Link l = new Link<Void>("logoutLink") {

			@Override
			public void onClick() {
				throw new RedirectToUrlException("/ui/logout");
			}

		};
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof User) {
			String username = ((User) principal).getUsername();
			add(new Label("username", username));
		} else {
			add(new Label("username", "no user"));
			l.setVisible(false);
		}
		add(l);


	}
}
