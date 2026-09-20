package com.javaproject.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private LoggingAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JdbcUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        // Define users if they don't already exist in DB
        if (!manager.userExists("bugs")) {
            UserDetails bugs = User.builder()
                    .username("bugs")
                    .password(passwordEncoder.encode("bunny"))
                    .roles("USER")
                    .build();
            manager.createUser(bugs);
        }

        if (!manager.userExists("daffy")) {
            UserDetails daffy = User.builder()
                    .username("daffy")
                    .password(passwordEncoder.encode("duck"))
                    .roles("USER", "MANAGER")
                    .build();
            manager.createUser(daffy);
        }

        return manager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/user/**").hasAnyRole("USER", "MANAGER")
                .requestMatchers("/secured/**").hasAnyRole("USER", "MANAGER")
                .requestMatchers("/manager/**").hasRole("MANAGER")
                .requestMatchers("/h2-console/**", "/", "/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/secured", true)
                .permitAll()
            )
            .logout(logout -> logout
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )
            .exceptionHandling(exception -> exception
                .accessDeniedHandler(accessDeniedHandler)
            );

        return http.build();
    }
}
