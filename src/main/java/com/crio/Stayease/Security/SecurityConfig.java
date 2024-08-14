package com.crio.Stayease.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    CustomUserDetailsService userService;

    @Autowired
    private JwtAuthenticationEntryPoint point;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(csrf->csrf.disable());

        httpSecurity.authenticationProvider(authenticationProvider());

        // httpSecurity.authorizeHttpRequests(configurer->configurer.
        //             requestMatchers("/register").
        //             permitAll().
        //             anyRequest().
        //             authenticated()
        //             );

        httpSecurity.authorizeHttpRequests(request->request.
                requestMatchers("/register","/error","/auth/login","/logout","/hotels").
                permitAll().
                requestMatchers("/admin/**").hasRole("ADMIN").
                requestMatchers("/manager/**").hasRole("HOTEL_MANAGER").
                requestMatchers("/user/**").hasAnyRole("CUSTOMER","ADMIN","HOTEL_MANAGER").
                anyRequest().
                authenticated()
            );
    //             .formLogin(form -> form
    //                 .loginPage("/login")
    //                 .loginProcessingUrl("/login")
    //                 .defaultSuccessUrl("/welcome", true)
    //                 .failureUrl("/login?error=true")
    // //                .successHandler(customAuthenticationSuccessHandler)
    //                 .permitAll()
    //             ).logout(logout -> logout
    //             .logoutUrl("/logout")
    //             . logoutSuccessUrl("/login?logout")
    //             .permitAll()
    //        httpSecurity.logout(logout->logout.logoutUrl("/logout").logoutSuccessUrl("/auth/login?logout").invalidateHttpSession(true).clearAuthentication(true));
            httpSecurity.exceptionHandling(ex->ex.authenticationEntryPoint(point));
            httpSecurity.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
            httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
   //       httpSecurity.httpBasic(Customizer.withDefaults());
         return httpSecurity.build();        
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
}
