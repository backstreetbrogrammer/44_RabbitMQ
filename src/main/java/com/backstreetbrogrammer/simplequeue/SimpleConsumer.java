package com.backstreetbrogrammer.simplequeue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class SimpleConsumer extends Thread {
    private final String queueName;

    public SimpleConsumer(final String queueName) {
        this.queueName = queueName;
    }

    @Override
    public void run() {
        try {
            sleep(5000L);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("--> Running consumer");

        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (final Connection connection = factory.newConnection();
             final Channel channel = connection.createChannel()) {

            channel.queueDeclare(queueName,
                    /*durable*/    false,
                    /*exclusive*/  false,
                    /*autoDelete*/ false,
                    /*arguments*/  null);

            System.out.println(" [*] Waiting for messages....");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.printf(" [x] Received '%s'%n", message);
            };

            channel.basicConsume(queueName,
                    /*autoAck*/ true,
                                 deliverCallback,
                                 consumerTag -> {
                                 });
        } catch (final IOException | TimeoutException e) {
            System.err.println(e.getMessage());
        }
    }
}
