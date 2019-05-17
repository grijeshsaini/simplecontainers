package com.simplecontainers.containers;

import com.google.common.collect.ImmutableMap;
import com.simplecontainers.SimpleContainersSpinnerRule;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

public class SimpleKafkaContainerTest {
    private static final SimpleKafkaContainer kafkaContainer = new SimpleKafkaContainer();

    @ClassRule
    public static SimpleContainersSpinnerRule simpleContainerSpinner = new SimpleContainersSpinnerRule(kafkaContainer);

    @Test
    public void shouldAbleToSendAndReadMessageFromTopic() {
        kafkaContainer.sendMessageToTopic("testTopic", "200", "KEY");
        ConsumerRecord<String, String> message = kafkaContainer.getMessageFromTopic("testTopic");

        Assert.assertEquals("200", message.value());
    }

    @Test
    public void shouldAbleToSendMessageWithHeaderAndReadHeadersFromTopic() {
        kafkaContainer.sendMessageToTopic("testTopic", "MESSAGE", "KEY", ImmutableMap.of("HEADER_KEY", "HEADER_VALUE"));

        ConsumerRecord<String, String> message = kafkaContainer.getMessageFromTopic("testTopic");

        Assert.assertEquals("MESSAGE", message.value());
        Assert.assertEquals("HEADER_VALUE", new String(message.headers().headers("HEADER_KEY").iterator().next().value()));
    }
}