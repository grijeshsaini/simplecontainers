package com.simplecontainers.containers;

import com.simplecontainers.SimpleContainersSpinnerRule;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

public class SimpleImageContainerTest {
    private static final SimpleImageContainer simpleImageContainer = new SimpleImageContainer("rmohr/activemq:5.12.0", 8161);

    @ClassRule
    public static SimpleContainersSpinnerRule simpleContainerSpinner = new SimpleContainersSpinnerRule(simpleImageContainer);

    @Test
    public void shouldAbleToReturnIpAndPort() {
        Assert.assertNotNull(simpleImageContainer.getExternalHost());
        Assert.assertNotNull(simpleImageContainer.getExternalPort());
        Assert.assertNotNull(simpleImageContainer.url());
    }
}