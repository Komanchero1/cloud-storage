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
                .allowedOrigins("http://localhost:8080")// с этого адреса будут обрабатываться запросы
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")//определяет какие методы разрешены с этого домена
                .allowedHeaders("*") //сервер разрешает все заголовки, которые могут быть отправлены в запросах
                .allowCredentials(true);//разрешена отправка учетных данных в запросах соответствующим этим правилам
    }
}
