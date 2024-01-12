package com.backstreetbrogrammer.pubsubrouting;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RoutingProducer extends Thread {

    private final String exchangeName;

    public RoutingProducer(final String exchangeName) {
        this.exchangeName = exchangeName;
    }

    @Override
    public void run() {
        System.out.println("--> Running producer");

        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5673);

        try (final Connection connection = factory.newConnection();
             final Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);

            channel.basicPublish(exchangeName, "info", null, "Information message".getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(exchangeName, "warning", null, "Warning message".getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(exchangeName, "error", null, "Error message".getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(exchangeName, "critical", null, "Critical message".getBytes(StandardCharsets.UTF_8));

            System.out.println(" [P] Sent 4 events");
        } catch (final IOException | TimeoutException e) {
            System.err.println(e.getMessage());
        }
    }

}
