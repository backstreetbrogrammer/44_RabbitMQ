package com.backstreetbrogrammer.workerqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class WorkerQueueProducer extends Thread {

    private final String queueName;

    public WorkerQueueProducer(final String queueName) {
        this.queueName = queueName;
    }

    @Override
    public void run() {
        System.out.println("--> Running producer");

        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5673);

        try (final Connection connection = factory.newConnection();
             final Channel channel = connection.createChannel()) {

            channel.queueDeclare(queueName,
                    /*durable*/    true,
                    /*exclusive*/  false,
                    /*autoDelete*/ false,
                    /*arguments*/  null);

            for (int i = 0; i <= 5; i++) {
                final String message = String.format("Hello Guidemy Students %d", i);

                channel.basicPublish(/*exchange*/"", /*routingKey*/ queueName,
                                                 MessageProperties.PERSISTENT_TEXT_PLAIN,
                                                 message.getBytes(StandardCharsets.UTF_8));
                System.out.printf(" [P] Sent '%s'%n", message);
                sleep(20L);
            }
        } catch (final IOException | TimeoutException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
}
