package com.backstreetbrogrammer.pubsubfanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class FanoutProducer extends Thread {
    private final String exchangeName;

    public FanoutProducer(final String exchangeName) {
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

            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);

            for (int i = 0; i <= 5; i++) {
                final String message = String.format("Hello Guidemy Students %d", i);
                channel.basicPublish(exchangeName, "", null, message.getBytes(StandardCharsets.UTF_8));
                System.out.printf(" [P] Sent '%s'%n", message);
                sleep(20L);
            }

        } catch (final IOException | TimeoutException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
}
