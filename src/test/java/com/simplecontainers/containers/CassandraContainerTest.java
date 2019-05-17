package com.simplecontainers.containers;

import com.datastax.driver.core.ResultSet;
import com.simplecontainers.SimpleContainersSpinnerRule;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class CassandraContainerTest {

    private static final CassandraContainer cassandraContainer = new CassandraContainer();

    @ClassRule
    public static SimpleContainersSpinnerRule simpleContainersSpinnerRule = new SimpleContainersSpinnerRule(cassandraContainer);

    @Test
    public void shouldAbleToReturnPropertiesOfContainer() {
        Assert.assertNotNull(cassandraContainer.getExternalHost());
        Assert.assertNotNull(cassandraContainer.getExternalPort());
        Assert.assertNotNull(cassandraContainer.url());
    }

    @Test
    public void shouldAbleToExecuteCQLStatements() {
        List<ResultSet> resultSets = cassandraContainer.executeCqlStatements(Collections.singletonList("SELECT release_version FROM system.local where key = 'local';"));

        Assert.assertTrue(resultSets.get(0).one().toString().contains("3.11.3"));
    }
}