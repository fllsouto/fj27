package br.com.alura.forum.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("fellipe")
            .password(new BCryptPasswordEncoder().encode("123456"))
            .authorities("ROLE_ADMIN")
        .and()
            .withUser("joao")
            .password(new BCryptPasswordEncoder().encode("123123"))
            .authorities("ROLE_USER")
        .and()
            .passwordEncoder(new BCryptPasswordEncoder());
    }
}
