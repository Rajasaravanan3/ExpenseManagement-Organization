package com.example.demo.ExpenseManagement.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.ExpenseManagement.Service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private JWTAuthenticationFilter authenticationFilter;

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/currencies/**", "/categories/**", "/budgets/**").permitAll()
                        .requestMatchers("/users/all/**", "/addresses", "/paymentMethods", "/currencies", "/categories", "/budgets", "/approvals/**").hasAuthority("ADMIN")

                        .requestMatchers("/expenses/approver/**").hasAnyAuthority("APPROVER", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/paymentMethods/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/organizations").hasAuthority("ADMIN")

                        .requestMatchers("/users**", "/addresses/**", "/organization**", "/expenses**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/roles**").hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
