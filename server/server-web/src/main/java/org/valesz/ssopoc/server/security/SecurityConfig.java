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
        // /ui/ is accessible to all
        // /auth/oauth/authorize is accessible only to logged users
        // /auth/oauth/token is accessible only to logged users
        // /ui/user is accessible only to logged users
//        http.csrf().disable()
//                    .requestMatchers()
//                        .antMatchers("/oauth/authorize").authenticated()
//                        .antMatchers("/ui/", "/ui/login", "/resources/**").permitAll()
//
//                        // token related endpoint are secured by spring configuration, if they're mapped to web root
//                        // so in this case /oauth/token, /oauth/token_key and /oauth/check_token endpoints are secured by autoconfig
//                        // but following three ones are not
////                        .antMatchers("/oauth/token").authenticated()
////                        .antMatchers("/oauth/token_key").access("permitAll()")
////                        .antMatchers("/oauth/check_token").access("isAuthenticated()")
//
//                        .antMatchers("/ui/user").authenticated()
//                        .antMatchers("/user/me").authenticated()
//                        .anyRequest().authenticated()
//                .and().formLogin().loginPage("/ui/login").permitAll();
//                .and().addFilter(new SecurityContextPersistenceFilter()).securityContext();

        http.csrf().disable()
                .requestMatchers().antMatchers("/auth/oauth/authorize")
                    .and()
                    .authorizeRequests()
                        .anyRequest().authenticated()
                    .and()
                    .formLogin().loginPage("/ui/login").permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.debug(true);
    }


}
