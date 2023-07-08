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

    // Authenticatie met customUserDetailsService en passwordEncoder
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    // PasswordEncoderBean. Deze kun je overal in je applicatie injecteren waar nodig.
    // Je kunt dit ook in een aparte configuratie klasse zetten.
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }
    // Authorizatie met jwt
    @Bean
    protected SecurityFilterChain filter (HttpSecurity http) throws Exception {

        //JWT token authentication
        http
                .csrf().disable()
                .httpBasic().disable()
                .cors().and()
                .authorizeHttpRequests()
                //Authentication
                .requestMatchers(HttpMethod.GET, "/authenticated").authenticated()   //                                 DOET HET
                .requestMatchers(HttpMethod.POST, "/login").permitAll() //iedereen kan proberen in te loggen.           DOET HET
                //User
//                .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN") //alleen admin kan alle users ophalen
                .requestMatchers(HttpMethod.GET, "/users/{username}").hasAnyRole("ADMIN", "USER")             // DOET HET
                .requestMatchers(HttpMethod.POST, "/users").permitAll()//iedereen kan zich registreren                  DOET HET
                .requestMatchers(HttpMethod.PUT, "/users/{username}").hasAnyRole("ADMIN", "USER")              //DOET HET
                .requestMatchers(HttpMethod.DELETE, "/users/{username}").hasAnyRole("ADMIN", "USER")        //  DOET HET
                .requestMatchers(HttpMethod.GET, "/users/{username}/authorities").hasRole("ADMIN")                  // DOET HET todo MOET IK NOG 403 forbidden response returnen voor user?
                .requestMatchers(HttpMethod.POST, "/users/{username}/authorities").hasRole("ADMIN")                 // DOET HET todo MOET IK NOG 403 forbidden response returnen voor user?
                .requestMatchers(HttpMethod.DELETE, "/users/{username}/authorities/{authority}").hasRole("ADMIN")   // DOET HET todo MOET IK NOG 403 forbidden response returnen voor user?
                //account
                .requestMatchers(HttpMethod.POST, "/accounts/createaccount").authenticated()
                .requestMatchers(HttpMethod.GET, "/accounts").hasRole("ADMIN")
                //volgende denk ik  admin, alhoewel ik bij een proposematch de info beschikbaar wil maken voor account, maar dat kan ik denk ik bij de match regelen?
                .requestMatchers(HttpMethod.GET, "/accounts/{username}").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/accounts/{username}").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/accounts/{username}/upload").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/accounts/{username}/upload").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/accounts/{username}").hasAnyRole("USER", "ADMIN")

                .anyRequest().denyAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}