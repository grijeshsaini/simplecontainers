package com.simplecontainers.containers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

public class SimpleKafkaContainer extends AbstractSimpleContainer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleKafkaContainer.class);
    private static final int KAFKA_PORT = 9093;
    private static final int ZOOKEEPER_PORT = 2181;

    @Override
    public GenericContainer getUnderlyingContainer() {
       return new KafkaContainer()
                .withExposedPorts(KAFKA_PORT, ZOOKEEPER_PORT).withStartupTimeout(Duration.ofSeconds(150L));
    }

    @Override
    public Integer getExposedPort() {
        return KAFKA_PORT;
    }

    public ConsumerRecord<String, String> getMessageFromTopic(String topic) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.url());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer-" + UUID.randomUUID().toString());
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));
        //noinspection deprecation
        consumer.poll(0); //Heartbeat //The new API of poll(Duration) is not working here, falling back to old option for now
        consumer.seekToBeginning(consumer.assignment());


        //noinspection deprecation
        ConsumerRecords<String, String> consumerRecords = consumer.poll(1000L);
        Optional<ConsumerRecord<String, String>> consumerRecord = StreamSupport.stream(consumerRecords.spliterator(), false)
                .reduce((first, last) -> last);// Always returning the last message
        consumer.close();

        return consumerRecord.orElseThrow(() -> new RuntimeException("Message not found"));
    }

    public void sendMessageToTopic(String topic, String message, String key) {
        sendMessageToTopic(topic, message, key, Collections.emptyMap());
    }

    public void sendMessageToTopic(String topic, String message, String key, Map<String, Object> headers) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.url()); //localhost:9092
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, null, key, message, toKafkaHeaders(headers));
        producer.send(producerRecord, callback.apply(topic, headers));
        producer.close();
    }

    @NotNull
    private final BiFunction<String, Map<String, Object>, Callback> callback = (topic, headers) -> (done, err) -> {
        if (err != null) {
            throw new RuntimeException("Error while sending message", err);
        } else {
            LOGGER.info("Sent message to " + topic + " with headers " + headers);
        }
    };

    private static Iterable<Header> toKafkaHeaders(Map<String, Object> headers){
        return headers.entrySet().stream()
                .filter(keyValue -> keyValue.getValue() != null)
                .map(keyValue -> new Header() {
                    @Override
                    public String key() {
                        return keyValue.getKey();
                    }

                    @Override
                    public byte[] value() {
                        return keyValue.getValue().toString().getBytes(StandardCharsets.UTF_8);
                    }
                })
                .collect(toList());
    }
}
