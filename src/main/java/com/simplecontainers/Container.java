package com.simplecontainers;

import org.testcontainers.containers.Network;

public interface Container {

    void start();

    void stop();

    String url();

    void withSharedNetwork(Network network);

    String getExternalHost();

    String getInternalHost();

    Integer getInternalPort();

    Integer getExternalPort();
}
