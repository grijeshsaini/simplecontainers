package com.simplecontainers.containers;

import com.simplecontainers.Container;
import com.simplecontainers.utils.JdbcUtils;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SimplePostgresContainer<SELF extends SimplePostgresContainer<SELF>>
        extends PostgreSQLContainer<SELF> implements Container, JdbcUtils {

    private Network sharedNetwork = null;

    public SimplePostgresContainer() {
        super();
    }

    public SimplePostgresContainer(String dockerImageWithVersion) {
        super(dockerImageWithVersion);
    }

    @Override
    public String url() {
        return super.getJdbcUrl();
    }

    @Override
    public void withSharedNetwork(Network network) {
        super.withNetwork(network);
    }

    @Override
    public String getExternalHost() {
        if (sharedNetwork != null) return super.getNetworkAliases().get(0);
        else return getInternalHost();
    }

    @Override
    public Integer getExternalPort() {
        if (sharedNetwork != null) return POSTGRESQL_PORT;
        else return getInternalPort();
    }

    @Override
    public String getInternalHost() {
        return super.getContainerIpAddress();
    }

    @Override
    public Integer getInternalPort() {
        return super.getMappedPort(POSTGRESQL_PORT);
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        return executeQuery(this, sql);
    }
}
