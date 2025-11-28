package com.icetea.MonStu.shared.config;


import com.icetea.MonStu.history.domain.History;
import com.icetea.MonStu.shared.security.details.CustomUserDetails;
import com.icetea.MonStu.shared.cache.CustomTTLCircleCache;
import com.icetea.MonStu.shared.cache.objects.HistoryCacheKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean   //유저 정보 저장 캐시
    public CustomTTLCircleCache<String, CustomUserDetails> customUserDetailsCache(){
        return new CustomTTLCircleCache<>(100,1000*50*10); // 10분, 100명
    }

    @Bean   //번역 기록 저장 캐시
    public CustomTTLCircleCache<HistoryCacheKey, History> customHistoryCache(){
        return new CustomTTLCircleCache<>(100,1000*50*10); // 10분, 100개
    }
}
