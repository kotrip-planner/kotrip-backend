package com.example.kotrip.config;

import com.example.kotrip.handler.CustomAccessDeniedHandler;
import com.example.kotrip.handler.JwtAuthenticationEntryPoint;
import com.example.kotrip.jwt.JwtTokenFilter;
import com.example.kotrip.jwt.JwtTokenProvider;
import com.example.kotrip.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic(HttpBasicConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .anyRequest().permitAll())
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider, customUserDetailsService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                )
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 특정 endpoint들은 인증하지 않아도 접근할 수 있도록 한다.
        return (web) -> web.ignoring().requestMatchers(
                "/api/login",
                "/api/reissue",
                "/api/kakao",
                "/api/naver/driving",
                "/city",
                "/tour",
                "/hotelSearch",

                // swagger 문서 관련 리소스 (전부 열어주어야 함)
                "/swagger-ui/**",
                "/swagger-resources/**", // css, js, png ...
                "/v3/api-docs/**"
        );
    }
}