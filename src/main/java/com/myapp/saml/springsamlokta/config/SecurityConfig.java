package com.myapp.saml.springsamlokta.config;

import org.springframework.security.extensions.saml2.config.SAMLConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.saml2.metadata-url}")
    String metadataUrl;

    @Value("${server.ssl.key-alias}")
    String alias;

    @Value("${server.ssl.key-store-password}")
    String pwd;

    @Value("${server.port}")
    String port;

    @Value("${server.ssl.key-store}")
    String keyFilePath;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/saml*").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(SAMLConfigurer.saml())
                .serviceProvider()
                .keyStore()
                .storeFilePath(this.keyFilePath)
                .password(this.pwd)
                .keyname(this.alias)
                .keyPassword(this.pwd)
                .and()
                .protocol("https")
                .hostname(String.format("%s:%s", "localhost", this.port))
                .basePath("/")
                .and()
                .identityProvider()
                .metadataFilePath(this.metadataUrl);
    }
}
