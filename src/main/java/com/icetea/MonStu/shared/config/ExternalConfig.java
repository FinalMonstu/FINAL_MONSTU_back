package com.icetea.MonStu.shared.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:credentials.properties")
public class ExternalConfig {
    // Bean 정의나 @ConfigurationProperties 클래스를 여기에 두면
    // credentials.properties 에 정의된 프로퍼티를 스프링이 읽어들입니다.
}
