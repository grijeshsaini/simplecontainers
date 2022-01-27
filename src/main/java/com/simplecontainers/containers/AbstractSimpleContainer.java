package com.simplecontainers.containers;

import com.simplecontainers.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;

public abstract class AbstractSimpleContainer implements Container {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());
    private GenericContainer underlyingContainer;
    private Network sharedNetwork;

    protected abstract GenericContainer getUnderlyingContainer();

    protected abstract Integer getExposedPort();

    @Override
    public void start() {
        underlyingContainer = getUnderlyingContainer();
        LOGGER.info("Starting underlying container {}", underlyingContainer.getDockerImageName());
        if (sharedNetwork != null) underlyingContainer.withNetwork(sharedNetwork);
        underlyingContainer.start();
        LOGGER.info("Started container {}", underlyingContainer.getDockerImageName());
    }

    @Override
    public void stop() {
        LOGGER.info("Stopping underlying container {}", underlyingContainer.getDockerImageName());
        underlyingContainer.stop();
        LOGGER.info("Stopped container {}", underlyingContainer.getDockerImageName());
    }

    @Override
    public String url() {
        return getExternalHost() + ":" + getExternalPort();
    }

    @Override
    public String getExternalHost() {
        return underlyingContainer.getContainerIpAddress();

    }

    @Override
    public Integer getExternalPort() {
        return underlyingContainer.getMappedPort(getExposedPort());
    }

    @Override
    public String getInternalHost() {
        if (sharedNetwork != null) return underlyingContainer.getNetworkAliases().get(0).toString();
        else return underlyingContainer.getContainerIpAddress();
    }

    @Override
    public Integer getInternalPort() {
        if (sharedNetwork != null) return getExposedPort();
        else return underlyingContainer.getMappedPort(getExposedPort());
    }

    @Override
    public void withSharedNetwork(Network sharedNetwork) {
        this.sharedNetwork = sharedNetwork;
    }
}
