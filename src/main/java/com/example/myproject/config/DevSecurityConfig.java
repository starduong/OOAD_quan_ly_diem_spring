package com.example.myproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Development Security Configuration - BYPASS ALL AUTHENTICATION
 * Chỉ active khi chạy với profile "dev"
 * 
 * Cách sử dụng:
 * 1. IntelliJ/Eclipse: Add VM option: -Dspring.profiles.active=dev
 * 2. Command line: mvn spring-boot:run -Dspring-boot.run.profiles=dev
 * 3. application.properties: spring.profiles.active=dev
 */
@Configuration
@Profile("dev") // Chỉ active khi profile = dev
public class DevSecurityConfig {

    @Bean
    public SecurityFilterChain devFilterChain(HttpSecurity http) throws Exception {
        System.out.println("⚠️⚠️⚠️ WARNING: RUNNING IN DEV MODE - AUTHENTICATION DISABLED! ⚠️⚠️⚠️");
        
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // ⚠️ CHO PHÉP TẤT CẢ REQUEST!
            )
            .anonymous(anonymous -> anonymous.disable()) // Disable anonymous filter
            .formLogin(form -> form.disable()) // Disable form login
            .httpBasic(basic -> basic.disable()) // Disable basic auth
            .logout(logout -> logout.disable()); // Disable logout
        
        return http.build();
    }
    
    /**
     * PasswordEncoder bean - cần thiết cho OtherController và các nơi khác dùng BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
