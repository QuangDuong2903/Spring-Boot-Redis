package com.quangduong.redis.repository;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class BaseTest {

    @Container
    static MySQLContainer mySQLContainer = (MySQLContainer) new MySQLContainer("mysql:8.0")
            .withDatabaseName("test-db")
            .withUsername("quangduong")
            .withPassword("292003")
            .withReuse(true);

    static {
        mySQLContainer.start();
    }


}
