package com.simplecontainers.containers;

import com.simplecontainers.rules.SimpleContainersSpinnerRule;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SimplePostgresContainerTest {

    private final static SimplePostgresContainer simplePostgresContainer = new SimplePostgresContainer<>().withDatabaseName("");

    @ClassRule
    public static SimpleContainersSpinnerRule simpleContainerSpinner = new SimpleContainersSpinnerRule(simplePostgresContainer);


    @Test
    public void shouldAbleToReturnPropertiesOfContainer() {
        Assert.assertNotNull(simplePostgresContainer.getExternalHost());
        Assert.assertNotNull(simplePostgresContainer.getExternalPort());
        Assert.assertNotNull(simplePostgresContainer.url());
    }

    @Test
    public void shouldExecuteQuery() throws SQLException {
        ResultSet resultSet = simplePostgresContainer.executeQuery(simplePostgresContainer.getTestQueryString());
        Assert.assertEquals(1, resultSet.getInt(1));
    }
}