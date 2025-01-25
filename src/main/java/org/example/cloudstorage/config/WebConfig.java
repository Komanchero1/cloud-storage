package org.example.cloudstorage.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //является классом конфигурацией в приложении Spring
@EnableWebMvc //включает функциональность Spring MVC
//класс для управления CORS
class WebConfig implements WebMvcConfigurer {

    @Override// метод переопределяет стандартное поведение CORS
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")// настройки применяются ко всем URL
                .allowCredentials(true)//разрешена отправка учетных данных в запросах соответствующим этим правилам
                .allowedOrigins("http://localhost:8081")// запросы только с этого адреса могут обращаться к ресурсам этого приложения
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");//определяет какие методы разрешены с этого домена
    }
}
