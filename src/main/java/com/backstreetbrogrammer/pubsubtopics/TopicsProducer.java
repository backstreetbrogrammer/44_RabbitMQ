package com.backstreetbrogrammer.pubsubtopics;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class TopicsProducer extends Thread {
    private final String exchangeName;

    public TopicsProducer(final String exchangeName) {
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

            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);

            channel.basicPublish(exchangeName, "application1.logs.error", null,
                                 "Sample Error Message from App1".getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(exchangeName, "application1.logs.info", null,
                                 "Sample Info Message from App1".getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(exchangeName, "application2.logs.error", null,
                                 "Sample Error Message from App2".getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(exchangeName, "application2.logs.debug", null,
                                 "Sample Debug Message from App2".getBytes(StandardCharsets.UTF_8));

            System.out.println(" [P] Sent 4 example messages");

        } catch (final IOException | TimeoutException e) {
            System.err.println(e.getMessage());
        }
    }
}
