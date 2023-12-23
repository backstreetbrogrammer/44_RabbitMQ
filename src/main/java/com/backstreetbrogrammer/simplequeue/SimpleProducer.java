package com.backstreetbrogrammer.simplequeue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class SimpleProducer extends Thread {

    private final String queueName;

    public SimpleProducer(final String queueName) {
        this.queueName = queueName;
    }

    @Override
    public void run() {
        System.out.println("--> Running producer");

        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (final Connection connection = factory.newConnection();
             final Channel channel = connection.createChannel()) {

            // alternative with TTL
            /*
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("x-message-ttl", 60000);
            channel.queueDeclare(queueName, false, false, false, args);
            */

            channel.queueDeclare(queueName,
                    /*durable*/    false,
                    /*exclusive*/  false,
                    /*autoDelete*/ false,
                    /*arguments*/  null);

            for (int i = 0; i <= 5; i++) {
                final String message = String.format("Hello Guidemy Students %d", i);

                channel.basicPublish(/*exchange*/"", /*routingKey*/ queueName,
                                                 null,
                                                 message.getBytes(StandardCharsets.UTF_8));
                System.out.printf(" [x] Sent '%s'%n", message);
                sleep(2000L);

                // alternative with TTL

                // final AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                //         .expiration("60000")
                //         .build();
                // channel.basicPublish(/*exchange*/ "", /*routingKey*/ queueName, properties, message.getBytes(StandardCharsets.UTF_8));
            }
        } catch (final IOException | InterruptedException | TimeoutException e) {
            System.err.println(e.getMessage());
        }
    }
}
