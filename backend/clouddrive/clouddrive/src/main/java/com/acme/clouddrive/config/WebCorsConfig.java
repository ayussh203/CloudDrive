// src/main/java/com/acme/clouddrive/config/WebCorsConfig.java
package com.acme.clouddrive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebCorsConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("http://localhost:5173")
        .allowedMethods("GET","POST","PATCH","DELETE","PUT","OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true);
  }
}
