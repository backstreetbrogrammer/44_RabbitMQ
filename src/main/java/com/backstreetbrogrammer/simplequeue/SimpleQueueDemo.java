package com.backstreetbrogrammer.simplequeue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SimpleQueueDemo {
    private final static String QUEUE_NAME = "hello_students_queue";

    public static void main(final String[] args) throws InterruptedException, IOException {
        loadSystemProperties();

        final SimpleProducer producer = new SimpleProducer(QUEUE_NAME);
        producer.start();

        final SimpleConsumer consumer = new SimpleConsumer(QUEUE_NAME);
        consumer.start();

        producer.join();
        consumer.join();

        System.out.println("Done");
    }

    private static void loadSystemProperties() throws IOException {
        final Properties p = new Properties();
        try (final InputStream inputStream = ClassLoader.getSystemResourceAsStream("rabbitmq.properties")) {
            p.load(inputStream);
        }
        for (final String name : p.stringPropertyNames()) {
            final String value = p.getProperty(name);
            System.setProperty(name, value);
        }
    }

}
