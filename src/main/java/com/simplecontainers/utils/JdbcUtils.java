package com.simplecontainers.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import org.testcontainers.containers.JdbcDatabaseContainer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface JdbcUtils {

    default ResultSet executeQuery(JdbcDatabaseContainer container, String sql) throws SQLException {
        HikariDataSource ds = getHikariDataSource(container);
        Statement statement = ds.getConnection().createStatement();
        statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();
        resultSet.next();
        return resultSet;
    }

    @NotNull
    default HikariDataSource getHikariDataSource(JdbcDatabaseContainer container) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(container.getJdbcUrl());
        hikariConfig.setUsername(container.getUsername());
        hikariConfig.setPassword(container.getPassword());
        hikariConfig.setDriverClassName(container.getDriverClassName());

        return new HikariDataSource(hikariConfig);
    }
}
