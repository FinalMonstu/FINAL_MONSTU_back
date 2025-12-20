package com.icetea.MonStu.shared.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // '/'로 요청이 들어오면
        registry.addViewController("/")
                .setViewName("forward:/index.html");
        // 1) 단일 세그먼트에 dot이 없고(static 제외)
        registry.addViewController("/{path:^(?!static|.*\\..*$).+}")
                .setViewName("forward:/index.html");

        // 2) 그 뒤에 오는 하위 경로도 동일하게
        registry.addViewController("/{path:^(?!static|.*\\..*$).+}/**")
                .setViewName("forward:/index.html");
    }
}