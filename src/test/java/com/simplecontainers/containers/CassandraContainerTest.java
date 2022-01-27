package com.simplecontainers.containers;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.simplecontainers.rules.SimpleContainersSpinnerRule;
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
        Assert.assertNotNull(cassandraContainer.getInternalHost());
        Assert.assertNotNull(cassandraContainer.getInternalPort());
        Assert.assertNotNull(cassandraContainer.url());
    }

    @Test
    public void shouldAbleToExecuteCQLStatements() {
        List<ResultSet> resultSets = cassandraContainer.executeCqlStatements(Collections.singletonList("SELECT release_version FROM system.local where key = 'local';"));

        Assert.assertEquals(resultSets.get(0).one().getString("release_version"), "3.11.3");
    }
}
