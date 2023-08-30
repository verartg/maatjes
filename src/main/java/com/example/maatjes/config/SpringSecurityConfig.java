package com.example.maatjes.config;

import com.example.maatjes.filter.JwtRequestFilter;
import com.example.maatjes.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain filter (HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .httpBasic().disable()
                .cors().and()
                .authorizeHttpRequests()

                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/authenticated").authenticated()

                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/users/{username}/authorities").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/users/{username}").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.GET, "/users/{username}/authorities").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/users/{username}").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.DELETE, "/users/{username}").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.DELETE, "/users/{username}/authorities/{authority}").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/accounts/createaccount").authenticated()
                .requestMatchers(HttpMethod.GET, "/accounts").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/accounts/{username}").authenticated()
                .requestMatchers(HttpMethod.GET, "/accounts/{username}/identification").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/accounts/{username}").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/accounts/{username}/identification").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/accounts/{username}/identification").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/accounts/{username}").hasAnyRole("USER", "ADMIN")

                .requestMatchers(HttpMethod.POST, "/matches").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/matches").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/matches/{matchId}").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/matches/{username}/accepted").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/matches/{username}/proposed").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/matches/{matchId}/accept").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/matches/{matchId}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/matches/{matchId}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/matches/expired").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/appointments/addappointment").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/appointments/match/{matchId}").hasRole("USER") 
                .requestMatchers(HttpMethod.GET, "/appointments/account/{accountId}").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/appointments/{appointmentId}").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/appointments/{appointmentId}").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/appointments/{appointmentId}").hasRole("USER")

                .requestMatchers(HttpMethod.POST, "/messages/{matchId}").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/messages/{matchId}").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/messages").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/reviews/new").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/reviews/{reviewId}").authenticated()
                .requestMatchers(HttpMethod.GET, "/reviews/by/{accountId}").authenticated()
                .requestMatchers(HttpMethod.GET, "/reviews/for/{accountId}").authenticated()
                .requestMatchers(HttpMethod.GET, "/reviews/toverify").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/reviews/verify/{reviewId}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/reviews/{reviewId}").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/reviews/{reviewId}").hasAnyRole("USER", "ADMIN")
                .anyRequest().denyAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}