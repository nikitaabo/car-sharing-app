package com.example.carsharingapp.config;

import org.testcontainers.containers.MySQLContainer;

public class SqlContainer extends MySQLContainer<SqlContainer> {
    private static final String DM_IMAGE = "mysql:8.0.33";
    private static SqlContainer mySqlContainer;

    public SqlContainer() {
        super(DM_IMAGE);
    }

    public static synchronized SqlContainer getInstance() {
        if (mySqlContainer == null) {
            mySqlContainer = new SqlContainer();
        }
        return mySqlContainer;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("TEST_DB_URL", mySqlContainer.getJdbcUrl());
        System.setProperty("TEST_DB_USERNAME", mySqlContainer.getUsername());
        System.setProperty("TEST_DB_PASSWORD", mySqlContainer.getPassword());
    }

    @Override
    public void stop() {
    }
}
