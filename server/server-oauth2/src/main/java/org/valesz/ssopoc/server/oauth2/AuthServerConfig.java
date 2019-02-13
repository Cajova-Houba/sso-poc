package org.valesz.ssopoc.server.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableAuthorizationServer
@PropertySource({"classpath:client.properties"})
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * For getting values from property file.
     */
    @Autowired
    private Environment env;

    /**
     * Token store is needed for revoking all tokens connected to logged user.
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(env.getProperty("client.id"))
                .secret(env.getProperty("client.secret"))
                .authorizedGrantTypes(env.getProperty("client.authorizedGrantTypes"))
                .scopes(env.getProperty("client.scopes"))
                .autoApprove(true)
                .redirectUris("http://localhost:8082/ui/login",
                        "http://localhost:8083/ui2/login",
                        "http://localhost:8080/login/oauth2/code/client");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // this is NEEDED when dispatcher servlet which manages spring's oauth2 stuff is mapped to anything other
        // than web root (/)
        // it will set the prefix which is then used for securing token endpoints in AuthorizationServerSecurityConfiguration
        // the prefix itself also used in web module in dispatcher servlet mapping in web.xml
        endpoints.getFrameworkEndpointHandlerMapping().setPrefix("/auth");
        endpoints.tokenStore(tokenStore());
    }


}
