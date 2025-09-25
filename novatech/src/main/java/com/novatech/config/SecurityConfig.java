package com.novatech.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final CustomLoginSuccessHandler successHandler;

        public SecurityConfig(CustomLoginSuccessHandler successHandler) {
                this.successHandler = successHandler;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/about", "/blog", "/contacto", "/", "/index",
                                                                "/producto/**", "/producto-detalle/**", "/css/**",
                                                                "/js/**", "/img/**",
                                                                "/login", "/registro", "/static/**")
                                                .permitAll()
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                .anyRequest().permitAll())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .successHandler(successHandler)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/")
                                                .permitAll())
                                .exceptionHandling()
                                .accessDeniedHandler(new RedirectAccessDeniedHandler("/")); // redirige al inicio

                return http.build();
        }

        // Handler para redirigir acceso denegado a "/"
        public static class RedirectAccessDeniedHandler implements AccessDeniedHandler {
                private final String redirectUrl;

                public RedirectAccessDeniedHandler(String redirectUrl) {
                        this.redirectUrl = redirectUrl;
                }

                @Override
                public void handle(HttpServletRequest request, HttpServletResponse response,
                                org.springframework.security.access.AccessDeniedException accessDeniedException)
                                throws IOException, ServletException {
                        response.sendRedirect(redirectUrl);
                }
        }
}
