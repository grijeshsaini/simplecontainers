package com.simplecontainers.network;

import org.testcontainers.containers.Network;

public class SimpleContainerNetwork {

    public static Network createSharedNetwork() {
        return Network.newNetwork();
    }
}
