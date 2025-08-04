package com.icetea.MonStu.config;


import com.icetea.MonStu.entity.History;
import com.icetea.MonStu.security.CustomUserDetails;
import com.icetea.MonStu.util.cache.CustomTTLCircleCache;
import com.icetea.MonStu.util.cache.objects.HistoryCacheKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public CustomTTLCircleCache<String, CustomUserDetails> customUserDetailsCache(){
        return new CustomTTLCircleCache<>(100,1000*50*10); // 10분, 100명
    }

    @Bean
    public CustomTTLCircleCache<HistoryCacheKey, History> customHistoryCache(){
        return new CustomTTLCircleCache<>(100,1000*50*10); // 10분, 100개
    }

}
