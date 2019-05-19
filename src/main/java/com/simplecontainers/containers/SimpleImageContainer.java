package com.simplecontainers.containers;

import org.testcontainers.containers.GenericContainer;

public class SimpleImageContainer extends AbstractSimpleContainer {

    private final Integer port;
    private final String dockerImageWithVersion;

    public SimpleImageContainer(String dockerImageWithVersion, Integer port) {
        this.port = port;
        this.dockerImageWithVersion = dockerImageWithVersion;
    }

    @Override
    public GenericContainer getUnderlyingContainer() {
      return new GenericContainer(dockerImageWithVersion)
                .withExposedPorts(port);
    }

    @Override
    public Integer getExposedPort() {
       return port;
    }
}
