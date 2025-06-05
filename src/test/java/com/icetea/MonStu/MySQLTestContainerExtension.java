package com.icetea.MonStu;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.MySQLContainer;

/* 모든 테스트가 컨테이너를 공유
*  @ExtendWith(MySQLTestContainerExtension.class)
* */

public class MySQLTestContainerExtension implements BeforeAllCallback {

    private static final MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.0.33")
                    .withDatabaseName("monstu_test")
                    .withUsername("test_user")
                    .withPassword("test_pw");

    @Override
    public void beforeAll(ExtensionContext context) {
        if (!mysql.isRunning()) { mysql.start(); }
        System.setProperty("spring.datasource.url",      mysql.getJdbcUrl());
        System.setProperty("spring.datasource.username", mysql.getUsername());
        System.setProperty("spring.datasource.password", mysql.getPassword());
        System.setProperty("spring.datasource.driver-class-name","com.mysql.cj.jdbc.Driver");
    }

    public static MySQLContainer<?> getMysql() { return mysql; }
}
