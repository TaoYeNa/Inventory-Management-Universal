package com.Ecommerceservice.discoveryservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // 使用 Lambda 表达式禁用 CSRF
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().authenticated())  // 配置请求授权
                .httpBasic(withDefaults());  // 使用默认配置启用 HTTP Basic 认证

        return http.build();

    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 不推荐使用 NoOpPasswordEncoder，生产环境应该使用 BCryptPasswordEncoder
        return NoOpPasswordEncoder.getInstance();
    }

}
