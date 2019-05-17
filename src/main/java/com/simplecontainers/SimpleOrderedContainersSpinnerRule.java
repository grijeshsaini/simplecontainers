package com.simplecontainers;

import org.junit.rules.ExternalResource;
import org.testcontainers.containers.Network;

import java.util.Arrays;
import java.util.List;

/**
 * This class is responsible for starting and stopping the containers.
 * It will start them in the ordered way.
 * It will create shared network which all container will use
 */
public class SimpleOrderedContainersSpinnerRule extends ExternalResource {

    private final List<Container> containers;

    public SimpleOrderedContainersSpinnerRule(Container... containers) {
        this.containers = Arrays.asList(containers);
    }

    public void spinContainers() {
        Network sharedNetwork = SimpleContainerNetwork.createSharedNetwork();
        containers.forEach(container -> {
            container.withSharedNetwork(sharedNetwork);
            container.start();
        });
    }

    public void stopContainers() {
        containers.parallelStream().forEach(Container::stop);
    }

    @Override
    protected void before() {
        spinContainers();
    }

    @Override
    protected void after() {
        stopContainers();
    }
}
