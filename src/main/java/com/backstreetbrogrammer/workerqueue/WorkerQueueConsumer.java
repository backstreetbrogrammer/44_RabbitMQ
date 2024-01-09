package com.backstreetbrogrammer.workerqueue;

import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class WorkerQueueConsumer extends Thread {

    private final String queueName;
    private final String workerName;

    public WorkerQueueConsumer(final String queueName, final String workerName) {
        this.queueName = queueName;
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

            final DeclareOk rc = channel.queueDeclare(queueName,
                    /*durable*/    true,
                    /*exclusive*/  false,
                    /*autoDelete*/ false,
                    /*arguments*/  null);

            System.out.printf(" [C] %s %d messages in the queue, waiting for messages....%n",
                              workerName,
                              rc.getMessageCount());

            channel.basicQos(/*prefetchCount*/1);

            final DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                final String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

                System.out.printf(" [C] %s received '%s'%n", workerName, message);
                try {
                    try {
                        final int randomInt = (int) (10.0 * Math.random());
                        sleep(1000L + randomInt * 10L);
                    } catch (final InterruptedException _ignored) {
                        interrupt();
                    }
                } finally {
                    System.out.printf(" [C] %s processed, acknowledging...%n", workerName);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    // channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, /*requeue*/true);
                    // channel.basicReject(delivery.getEnvelope().getDeliveryTag(), true);
                }
            };

            channel.basicConsume(queueName,
                                 false,
                                 deliverCallback,
                                 consumerTag -> {
                                 });

            //sleep(Long.MAX_VALUE);
            sleep(12_000L);

        } catch (final IOException | TimeoutException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

}
