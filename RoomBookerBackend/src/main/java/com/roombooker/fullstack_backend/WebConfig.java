package com.roombooker.fullstack_backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	  registry.addResourceHandler("/uploads/**")
          .addResourceLocations("file:C:/Users/Asus/Desktop/fullstack-backend/uploads/");

    }
}

//ovo je konfiguracioni fajl koji definise gde ce se traziti resursi u cijem se zahtevu nalazi 
//uploads/**   , trazice se na putanji file:C:/Users/Asus/Desktop/fullstack-backend/uploads/
