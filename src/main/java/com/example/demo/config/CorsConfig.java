package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Allow all origins (you can restrict this to specific origins in production)
        config.addAllowedOriginPattern("*");
        
        // Allow all headers
        config.addAllowedHeader("*");
        
        // Allow all methods
        config.addAllowedMethod("*");
        
        // Allow credentials (cookies, authorization headers)
        config.setAllowCredentials(true);
        
        // Expose specific headers if needed
        // config.addExposedHeader("X-Custom-Header");
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}