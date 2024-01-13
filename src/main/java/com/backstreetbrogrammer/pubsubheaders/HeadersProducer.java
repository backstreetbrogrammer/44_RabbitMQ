package com.backstreetbrogrammer.pubsubheaders;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class HeadersProducer extends Thread {

    private final String exchangeName;

    public HeadersProducer(final String exchangeName) {
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

            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.HEADERS);

            publishSampleMessage(channel, "error", "Sample Error Message from App1");
            publishSampleMessage(channel, "info", "Sample Info Message from App1");
            publishSampleMessage(channel, "error", "Sample Error Message from App2");
            publishSampleMessage(channel, "debug", "Sample Debug Message from App2");

            System.out.println(" [P] Sent 4 example messages");
        } catch (final IOException | TimeoutException e) {
            System.err.println(e.getMessage());
        }
    }

    private void publishSampleMessage(final Channel channel, final String header, final String message) throws IOException {
        final Map<String, Object> map = new HashMap<>();
        map.put("x-match", "any");   //all or any
        map.put("my-header-severity", header);
        map.put("my-custom-header", "hello");
        final AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .headers(map)
                .build();

        channel.basicPublish(exchangeName, /*routingKey*/"", props, message.getBytes(StandardCharsets.UTF_8));
    }
}
