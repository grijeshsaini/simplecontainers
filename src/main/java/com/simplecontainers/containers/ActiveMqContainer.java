package com.simplecontainers.containers;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;

import javax.jms.*;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class ActiveMqContainer extends AbstractSimpleContainer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveMqContainer.class);
    private static final int ACTIVEMQ_PORT = 8161;
    private static final int ACTIVEMQ_TCP_PORT = 61616;
    private final String activeMqImage;

    public ActiveMqContainer() {
        this.activeMqImage = "rmohr/activemq:5.15.6";
    }

    public ActiveMqContainer(String activeMqImage) {
        this.activeMqImage = activeMqImage;
    }

    @Override
    public GenericContainer getUnderlyingContainer() {
        return new GenericContainer(activeMqImage)
                .withExposedPorts(ACTIVEMQ_PORT, ACTIVEMQ_TCP_PORT);
    }

    @Override
    public Integer getExposedPort() {
        return ACTIVEMQ_TCP_PORT;
    }

    @Override
    public String url() {
        return "tcp://" + super.url();
    }

    public void sendMessageToQueue(String message, String queueName) throws JMSException {
        sendMessageToQueue(message, queueName, Collections.emptyMap());
    }

    public void sendMessageToQueue(String message, String queueName, Map<String, String> messagePropertyToValue) throws JMSException {
        MessageProducer producer = null;
        Connection connection = null;
        Session session = null;
        try {
            connection = createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination controlQueue = session.createQueue(queueName);
            producer = session.createProducer(controlQueue);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            TextMessage textMessage = session.createTextMessage(message);
            textMessage.setText(message);
            messagePropertyToValue.forEach((key, value) -> {
                try {
                    textMessage.setStringProperty(key, value);
                } catch (JMSException e) {
                    LOGGER.error("Error while adding message properties", e);
                }
            });
            producer.send(textMessage);
        } finally {
            if (producer != null) producer.close();
            if (session != null) session.close();
            if (connection != null) connection.close();
        }

    }

    public String getMessagePropertyFromQueue(String queue, String property) throws JMSException {
        Optional<TextMessage> message = getMessage(queue);
        Optional<String> queueProperty = Optional.empty();
        if (message.isPresent()) {
            queueProperty = Optional.ofNullable(message.get().getStringProperty(property));
        }

        return queueProperty.orElse("FAILED TO FETCH MESSAGE PROPERTY FROM QUEUE");
    }

    public String getTextMessageFromQueue(String queue) throws JMSException {
        Optional<TextMessage> message = getMessage(queue);
        Optional<String> queueMessage = Optional.empty();
        if (message.isPresent()) {
            queueMessage = Optional.ofNullable(message.get().getText());
        }

        return queueMessage.orElse("FAILED TO FETCH MESSAGE FROM QUEUE");
    }

    private Optional<TextMessage> getMessage(String queue) throws JMSException {
        Connection connection = null;
        Session session = null;
        MessageConsumer consumer = null;
        Optional<TextMessage> textMessage = Optional.empty();
        try {
            connection = createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination controlQueue = session.createQueue(queue);
            consumer = session.createConsumer(controlQueue);
            Message message = consumer.receive(1000L);
            if (message instanceof TextMessage) {
                textMessage = Optional.of((TextMessage) message);
            }

        } finally {
            if (consumer != null) consumer.close();
            if (session != null) session.close();
            if (connection != null) connection.close();
        }

        return textMessage;
    }

    private Connection createConnection() throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.url());
        Connection connection = connectionFactory.createConnection();
        connection.start();
        return connection;
    }

}
