package com.syw.APIrest.Seguridad.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class CORSConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Permitir solicitudes desde todos los dominios, puedes ajustar esto según tus necesidades
        config.addAllowedOrigin("*");

        // Permitir ciertos métodos HTTP (GET, POST, PUT, DELETE, etc.)
        config.addAllowedMethod("*");

        // Permitir ciertos encabezados
        config.addAllowedHeader("*");

        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
    @Bean
    public ExecutorService battleExecutor() {
        return Executors.newFixedThreadPool(50); // Puedes ajustar el número de hilos según tus necesidades
    }
}

