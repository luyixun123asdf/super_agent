package com.example.my_super_agent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 运行Cooke
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedMethods("*")
                // 允许跨域的域名，可以用*表示允许任何域名使用
                .allowedOriginPatterns("*")
                .exposedHeaders("*");
    }
}
