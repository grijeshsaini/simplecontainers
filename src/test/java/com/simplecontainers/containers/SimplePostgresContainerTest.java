package com.simplecontainers.containers;

import com.simplecontainers.SimpleContainersSpinnerRule;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

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
}