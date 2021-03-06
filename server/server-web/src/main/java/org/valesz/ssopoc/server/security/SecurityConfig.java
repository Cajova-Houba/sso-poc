/*
 * Copyright 2017 dbeer.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.valesz.ssopoc.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 *
 * @author dbeer
 */
@Configuration
@Order(-1)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public SecurityConfig() {
        super(false);
    }

    @Autowired(required = false)
    private LogoutSuccessHandler oauth2PostLogout;

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user").password("password").roles("USER").build());
        return manager;
    }

    @Bean(name = "authenticationManger")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String uiPrefix = "";
        http.csrf().disable()
                .requestMatchers().antMatchers("/auth/oauth/authorize", "/","/login", "/signout", "/user")
                    .and()
                    .authorizeRequests()
                        .antMatchers(uiPrefix+"/").permitAll()
                        .antMatchers(uiPrefix+"/login").anonymous()
                        .antMatchers(uiPrefix+"/signout").authenticated()
                        .anyRequest().authenticated()
                    .and()
                    .formLogin()
                        .loginPage(uiPrefix+"/login")
                        .defaultSuccessUrl(uiPrefix+"/")
                    .and()
//                    .logout()
//                        .logoutUrl(uiPrefix+"/logout")
//                        .logoutSuccessUrl(uiPrefix+"/")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID", "UISESSION")
        ;

        if (oauth2PostLogout != null) {
            http.logout().logoutSuccessHandler(oauth2PostLogout);
        }
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.debug(false);
    }


}
