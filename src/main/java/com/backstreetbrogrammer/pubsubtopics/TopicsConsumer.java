package com.backstreetbrogrammer.pubsubtopics;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class TopicsConsumer extends Thread {

    private final String exchangeName;
    private final String workerName;
    private final List<String> topics;

    public TopicsConsumer(final String exchangeName, final String workerName, final List<String> topics) {
        this.exchangeName = exchangeName;
        this.workerName = workerName;
        this.topics = topics;
    }

    @Override
    public void run() {
        System.out.println("--> Running consumer");

        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5673);

        try (final Connection connection = factory.newConnection();
             final Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
            final String queueName = channel.queueDeclare().getQueue();

            for (final String topic : topics) {
                channel.queueBind(queueName, exchangeName, topic);
            }
            System.out.printf(" [C] %s, waiting for messages....%n", workerName);

            final DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                final String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.printf(" [C] %s received '%s':'%s'%n", workerName,
                                  delivery.getEnvelope().getRoutingKey(),
                                  message);
            };

            channel.basicConsume(queueName,
                                 true,
                                 deliverCallback,
                                 consumerTag -> {
                                 });

            //sleep(Long.MAX_VALUE);
            sleep(12_000L);

        } catch (final TimeoutException | IOException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
}
