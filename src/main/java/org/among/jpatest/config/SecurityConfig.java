package org.among.jpatest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 방지(컨트롤러 로그인 사용)
                .logout(AbstractHttpConfigurer::disable) // 커스텀 로그아웃(기본 로그아웃 필터 미사용)
                .csrf(AbstractHttpConfigurer::disable); // 커스텀 로그인 프로세스(POST) 403 방지

        http
                .authorizeHttpRequests(req -> req
                        .requestMatchers(HttpMethod.POST, "/dept/members").permitAll()
                        .requestMatchers("/user-info").permitAll() // 테스트 위해 허용
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
