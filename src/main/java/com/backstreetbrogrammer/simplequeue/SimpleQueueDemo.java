package com.backstreetbrogrammer.simplequeue;

public class SimpleQueueDemo {
    private final static String QUEUE_NAME = "hello_students_queue";

    public static void main(final String[] args) throws InterruptedException {
        SimpleProducer producer = new SimpleProducer(QUEUE_NAME);
        producer.start();

        SimpleConsumer consumer = new SimpleConsumer(QUEUE_NAME);
        consumer.start();

        producer.join();
        consumer.join();

        System.out.println("Done");
    }

}
