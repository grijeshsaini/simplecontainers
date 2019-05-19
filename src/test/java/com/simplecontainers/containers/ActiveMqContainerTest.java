package com.simplecontainers.containers;

import com.google.common.collect.ImmutableMap;
import com.simplecontainers.rules.SimpleContainersSpinnerRule;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import javax.jms.JMSException;

public class ActiveMqContainerTest {
    private static final ActiveMqContainer activeMqContainer = new ActiveMqContainer("rmohr/activemq:5.12.0");
    private static final ActiveMqContainer activeMqContainerDefault = new ActiveMqContainer();

    @ClassRule
    public static SimpleContainersSpinnerRule simpleContainerSpinner = new SimpleContainersSpinnerRule(activeMqContainer, activeMqContainerDefault);

    @Test
    public void shouldAbleToSendAndReadMessageFromQueue() throws JMSException {
        activeMqContainer.sendMessageToQueue("TEST_MESSAGE", "testQueue");
        Assert.assertEquals("TEST_MESSAGE", activeMqContainer.getTextMessageFromQueue("testQueue"));

        activeMqContainerDefault.sendMessageToQueue("TEST_MESSAGE_DEFAULT", "testQueue");

        Assert.assertEquals("TEST_MESSAGE_DEFAULT", activeMqContainerDefault.getTextMessageFromQueue("testQueue"));
    }

    @Test
    public void shouldAbleToReadMessagePropertyFromQueue() throws JMSException {
        activeMqContainer.sendMessageToQueue("TEST_MESSAGE", "testQueue", ImmutableMap.of("TEST_KEY", "TEST_VALUE"));

        Assert.assertEquals("TEST_VALUE", activeMqContainer.getMessagePropertyFromQueue("testQueue", "TEST_KEY"));
    }
}