package com.backstreetbrogrammer.pubsubfanout;

import com.rabbitmq.client.AMQP.Queue.BindOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class FanoutConsumer extends Thread {

    private final String exchangeName;
    private final String workerName;

    public FanoutConsumer(final String exchangeName, final String workerName) {
        this.exchangeName = exchangeName;
        this.workerName = workerName;
    }

    @Override
    public void run() {
        System.out.println("--> Running consumer");

        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5673);

        try (final Connection connection = factory.newConnection();
             final Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(exchangeName, "fanout");

            final String queueName = channel.queueDeclare().getQueue();
            final BindOk rc = channel.queueBind(queueName, exchangeName, /*routingKey*/"");

            System.out.printf(" [C] %s, waiting for messages....%n", workerName);

            final DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                final String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.printf(" [C] %s received '%s'%n", workerName, message);
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
