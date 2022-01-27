package com.simplecontainers.containers;

import com.simplecontainers.rules.SimpleOrderedContainersSpinnerRule;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Map;

public class SimpleKafkaContainerTest {
    private static final SimpleKafkaContainer kafkaContainer = new SimpleKafkaContainer();

    private static final ActiveMqContainer activeMqContainer = new ActiveMqContainer();

    @ClassRule
    public static SimpleOrderedContainersSpinnerRule simpleContainerSpinner = new SimpleOrderedContainersSpinnerRule(kafkaContainer, activeMqContainer);

    @Test
    public void shouldAbleToSendAndReadMessageFromTopic() {
        kafkaContainer.getInternalHost();
        kafkaContainer.sendMessageToTopic("testTopic", "200", "KEY");
        ConsumerRecord<String, String> message = kafkaContainer.getMessageFromTopic("testTopic");

        Assert.assertEquals("200", message.value());
    }

    @Test
    public void shouldAbleToSendMessageWithHeaderAndReadHeadersFromTopic() {
        kafkaContainer.sendMessageToTopic("testTopic", "MESSAGE", "KEY", Map.of("HEADER_KEY", "HEADER_VALUE"));

        ConsumerRecord<String, String> message = kafkaContainer.getMessageFromTopic("testTopic");

        Assert.assertEquals("MESSAGE", message.value());
        Assert.assertEquals("HEADER_VALUE", new String(message.headers().headers("HEADER_KEY").iterator().next().value()));
    }
}