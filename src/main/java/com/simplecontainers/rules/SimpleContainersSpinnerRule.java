package com.simplecontainers.rules;

import com.simplecontainers.Container;
import org.junit.rules.ExternalResource;

import java.util.Arrays;
import java.util.List;

public class SimpleContainersSpinnerRule extends ExternalResource {

    private final List<Container> containers;

    public SimpleContainersSpinnerRule(Container... containers) {
        this.containers = Arrays.asList(containers);
    }

    public void spinContainers() {
        containers.parallelStream().forEach(Container::start);
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
