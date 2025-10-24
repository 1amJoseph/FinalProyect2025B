package com.restaurante.ProyectRestaurante.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // OPTIONS público
                        .requestMatchers("/api/usuarios/**").permitAll()       // registro/login público
                        .requestMatchers("/api/mesas/**").permitAll()          // mesas público temporal
                        .requestMatchers("/api/productos/**").permitAll()      // productos público temporal
                        .requestMatchers("/api/roles/**").permitAll()          // roles público temporal
                        .requestMatchers("/api/recibos/**").permitAll()          // roles público temporal
                        .requestMatchers("/api/detalles/**").permitAll()          // roles público temporal
                        .anyRequest().authenticated()                           // resto protegido
                );

        return http.build();
    }
}
