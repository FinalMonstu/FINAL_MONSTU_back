package com.icetea.MonStu.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
@EnableMongoAuditing // @CreatedDate, @LastModifiedDate 기능을 활성화
public class MongoConfig {

    //MongoDB에 저장될 때 자동으로 생성되는 _class 필드를 제거
    @Bean
    public MappingMongoConverter mappingMongoConverter( MongoDatabaseFactory mongoDatabaseFactory, MongoMappingContext mongoMappingContext ) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);

        converter.setTypeMapper(new DefaultMongoTypeMapper(null));

        return converter;
    }

    /**
     * MongoDB 트랜잭션 매니저 설정입니다.
     * MongoDB Atlas M0(무료 티어)는 'Replica Set' 환경이므로 트랜잭션을 지원합니다.
     * 이 설정을 해두면 MongoDB 작업 시에도 @Transactional 어노테이션을 사용할 수 있습니다.
     */
    /*
    *   예: @Transactional(transactionManager = "transactionManager") (MongoDB용)
        예: @Transactional (기본 MySQL용)
    * */
    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }
}