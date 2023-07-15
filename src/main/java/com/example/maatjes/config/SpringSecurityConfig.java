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
                .requestMatchers(HttpMethod.GET, "/users/{username}/authorities").hasRole("ADMIN")                  // DOET HET
                .requestMatchers(HttpMethod.POST, "/users/{username}/authorities").hasRole("ADMIN")                 // DOET HET
                .requestMatchers(HttpMethod.DELETE, "/users/{username}/authorities/{authority}").hasRole("ADMIN")   // DOET HET
                //account
                .requestMatchers(HttpMethod.POST, "/accounts/createaccount").authenticated()                        //DOET HET
                .requestMatchers(HttpMethod.GET, "/accounts").hasRole("ADMIN")                                      //DOET HET
                .requestMatchers(HttpMethod.GET, "/accounts/{username}").authenticated()                            //DOET HET
                .requestMatchers(HttpMethod.GET, "/accounts/{username}/identification").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/accounts/{username}").hasAnyRole("USER", "ADMIN")            //DOET HET
                .requestMatchers(HttpMethod.PUT, "/accounts/{username}/identification").hasAnyRole("USER", "ADMIN")     //DOET HET
                .requestMatchers(HttpMethod.DELETE, "/accounts/{username}/identification").hasAnyRole("USER", "ADMIN")  //DOET HET
                .requestMatchers(HttpMethod.DELETE, "/accounts/{username}").hasAnyRole("USER", "ADMIN")         //DOET HET
                //match
                .requestMatchers(HttpMethod.POST, "/matches").hasRole("ADMIN")                                          //DOET HET
                .requestMatchers(HttpMethod.GET, "/matches").hasRole("ADMIN")                                           //DOET HET
                .requestMatchers(HttpMethod.GET, "/matches/{matchId}").hasAnyRole("USER", "ADMIN")              //DOET HET
                .requestMatchers(HttpMethod.GET, "/matches/{username}/accepted").hasRole("USER")                    //DOET HET
                .requestMatchers(HttpMethod.GET, "/matches/{username}/proposed").hasRole("USER")                    //DOET HET
                .requestMatchers(HttpMethod.PUT, "/matches/{matchId}/accept").hasRole("USER")                       //DOET HET
                .requestMatchers(HttpMethod.PUT, "/matches/{matchId}").hasRole("ADMIN")                             //DOET HET
                .requestMatchers(HttpMethod.DELETE, "/matches/{matchId}").hasRole("ADMIN")                          //DOET HET
                .requestMatchers(HttpMethod.DELETE, "/matches/expired").hasRole("ADMIN")                            //DOET HET
                //appointment
                .requestMatchers(HttpMethod.POST, "/appointments/addappointment").hasRole("USER")                   //DOET HET
                .requestMatchers(HttpMethod.GET, "/appointments/match/{matchId}").hasRole("USER")                   //DOET HET
                .requestMatchers(HttpMethod.GET, "/appointments/account/{accountId}").hasRole("USER")               //DOET HET
                .requestMatchers(HttpMethod.GET, "/appointments/{appointmentId}").hasRole("USER")                   //DOET HET
                .requestMatchers(HttpMethod.PUT, "/appointments/{appointmentId}").hasRole("USER")                   //DOET HET
                .requestMatchers(HttpMethod.DELETE, "/appointments/{appointmentId}").hasRole("USER")                //DOET HET
                //message
                .requestMatchers(HttpMethod.POST, "/messages/{matchId}").hasRole("USER")                            //DOET HET
                .requestMatchers(HttpMethod.GET, "/messages/{matchId}").hasRole("USER")                             //DOET HET
                .requestMatchers(HttpMethod.DELETE, "/messages").hasRole("ADMIN")                                   //DOET HET
                //review
                .requestMatchers(HttpMethod.POST, "/reviews/new").hasRole("USER")                                   //DOET HET
                .requestMatchers(HttpMethod.GET, "/reviews/{reviewId}").authenticated()                             //DOET HET
                .requestMatchers(HttpMethod.GET, "/reviews/by/{accountId}").authenticated()                         //DOET HET
                .requestMatchers(HttpMethod.GET, "/reviews/for/{accountId}").authenticated()                        //DOET HET
                .requestMatchers(HttpMethod.GET, "/reviews/toverify").hasRole("ADMIN")                              //DOET HET
                .requestMatchers(HttpMethod.PUT, "/reviews/verify/{reviewId}").hasRole("ADMIN")                     //DOET HET
                .requestMatchers(HttpMethod.PUT, "/reviews/{reviewId}").hasRole("USER")                             //DOET HET
                .requestMatchers(HttpMethod.DELETE, "/reviews/{reviewId}").hasAnyRole("USER", "ADMIN")       //DOET HET
                .anyRequest().denyAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}