package com.newbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class NewBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewBackendApplication.class, args);
    }

    @Configuration
    public static class WebConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            // 配置静态文件服务，用于访问上传的图片
            registry.addResourceHandler("/upload/**")
                    .addResourceLocations("file:" + System.getProperty("user.dir") + "/upload/");
        }
    }

}