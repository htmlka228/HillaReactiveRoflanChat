package com.example.application.security;

import com.example.application.service.UserService;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;

import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;
import java.util.Base64;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends VaadinWebSecurityConfigurerAdapter {
    @Value("${app.secret}")
    private String appSecret;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        setLoginView(http, "/login");
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        setStatelessAuthentication(
                http,
                new SecretKeySpec(Base64.getDecoder().decode(appSecret), JwsAlgorithms.HS256), //TODO Change encrypt method if it possible
                "com.example.application"
        );
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().antMatchers("/images/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource())
                .usersByUsernameQuery("select username, password, enabled from roflan_user where username = ?")
                .authoritiesByUsernameQuery("select roflan_user.username, role.name from roflan_user_roles" +
                        " join roflan_user on roflan_user.uuid = roflan_user_roles.roflan_user_uuid" +
                        " join role on role.id = roflan_user_roles.roles_id" +
                        " where username = ?"
                )
                .rolePrefix("ROLE_")
                .passwordEncoder(bCryptPasswordEncoder());
    }

    //TODO JDBC in reactive apps... Nice approach, Hilla =)
    public DataSource dataSource() {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName("org.postgresql.Driver");
        driver.setUrl("jdbc:postgresql://localhost:5432/roflan_chat");
        driver.setUsername("postgres");
        driver.setPassword("postgres");

        return driver;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
