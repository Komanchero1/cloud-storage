package org.example.cloudstorage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //класс конфигурация для Spring
@EnableWebSecurity //подключается конфигурация безопасности  Spring для приложения
public class SecurityConfig {

    @Bean // этот метод создает и возвращает бин который управляется Spring ом
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) //отключается защита от CSFR
                .authorizeHttpRequests(authorize -> authorize//настройка авторизации для HTTP запросов
                        .requestMatchers("/login", "/logout").permitAll() // разрешение доступа к /login и /logout без авторизации
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // разрешение для OPTIONS для всех типов URL
                        .anyRequest().authenticated()); // все остальные запросы требуют аутентификации
        return http.build();//возвращается объект SecurityFilterChain
    }


    @Bean // создается бин для шифрования паролей
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Использование BCrypt для шифрования паролей
    }


    @Bean // бин для управления аутентификацией
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);//получается объект AuthenticationManagerBuilder из HttpSecurity
        return authenticationManagerBuilder.build(); //возвращает объект AuthenticationManager
    }
}
