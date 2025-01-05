package com.example.marketplace_crm.Config;

import com.example.marketplace_crm.Service.Impl.MyUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final MyUserDetailService myUserDetailService;
    private final EncoderConfig encoderConfig;

    public SecurityConfig(MyUserDetailService myUserDetailService, EncoderConfig encoderConfig) {
        this.myUserDetailService = myUserDetailService;
        this.encoderConfig = encoderConfig;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
            .httpBasic().disable()
            .csrf().disable()
            .authorizeHttpRequests(auths -> auths
                    .requestMatchers("/products/**", "/users/**", "/categories/**", "/orders/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/css/**", "/images/**").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin()
            .loginPage("/auth/login")
            .permitAll()
            .and()
            .logout()
            .permitAll()
            .and()
            .build();
    }


    @Bean
    public UserDetailsService userDetailService() {
        return myUserDetailService;
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService());
        daoAuthenticationProvider.setPasswordEncoder(encoderConfig.bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

}
