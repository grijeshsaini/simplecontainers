package com.simplecontainers.containers;

import org.testcontainers.containers.GenericContainer;

public class RedisContainer extends AbstractSimpleContainer {

    private static final int REDIS_PORT = 6379;
    private final String redisImage;

    public RedisContainer() {
        this("redis:5.0.5-alpine");
    }

    public RedisContainer(String redisImage) {
        this.redisImage = redisImage;
    }

    @Override
    public GenericContainer getUnderlyingContainer() {
        return new GenericContainer(redisImage)
                .withExposedPorts(REDIS_PORT);
    }

    @Override
    public Integer getExposedPort() {
        return REDIS_PORT;
    }
}
