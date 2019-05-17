package com.simplecontainers.containers;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.simplecontainers.AbstractSimpleContainer;
import org.testcontainers.containers.GenericContainer;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CassandraContainer extends AbstractSimpleContainer {

    private static final int CASSANDRA_PORT = 9042;
    private final String cassandraImage;

    public CassandraContainer(String cassandraImage) {
        this.cassandraImage = cassandraImage;
    }

    public CassandraContainer() {
        this.cassandraImage = "cassandra:3.11.3";
    }

    @Override
    public GenericContainer getUnderlyingContainer() {
        return new GenericContainer(cassandraImage)
                .withExposedPorts(CASSANDRA_PORT)
                .withStartupAttempts(3)
                .withStartupTimeout(Duration.ofSeconds(360L));
    }

    @Override
    public Integer getExposedPort() {
        return CASSANDRA_PORT;
    }

    public List<ResultSet> executeCqlStatements(List<String> cqlStatements) {
        Cluster cluster = Cluster.builder()
                .addContactPoint(getExternalHost())
                .withPort(this.getExternalPort())
                .build();

        Session session = cluster.connect();
        List<ResultSet> resultSets = cqlStatements.stream().map(session::execute).collect(Collectors.toList());
        Collections.reverse(resultSets);
        return resultSets;
    }
}