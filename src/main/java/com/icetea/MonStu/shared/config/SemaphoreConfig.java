package com.icetea.MonStu.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Semaphore;

@Configuration
public class SemaphoreConfig {

    @Bean("translatePermits")
    public Semaphore translatePermits() { return new Semaphore(20, false); }

}
