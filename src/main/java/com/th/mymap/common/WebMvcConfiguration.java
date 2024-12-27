package com.th.mymap.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Slf4j
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer { //새로고침

/*    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("file:///D:/home/download/");
        //외부 파일 불러올 때 필요한 설정 (?)
    }*/

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/**")
                .addResourceLocations("classpath:/static/**")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource requestedResource = location.createRelative(resourcePath);

                        if (requestedResource.exists() && requestedResource.isReadable()) {
                            return requestedResource;
                        }

                        return new ClassPathResource("/static/index.html");
                    }
                });

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///D:/home/download/");
    }

/*    //cors설정 - 전역
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "DELETE", "UPDATE") // 허용할 HTTP method
                .allowCredentials(true); // 쿠키 인증 요청 허용
    }*/
}
