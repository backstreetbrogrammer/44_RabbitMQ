package com.backstreetbrogrammer.pubsubfanout;

public class PublishSubscribeDemo {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(final String[] args) throws InterruptedException {
        final FanoutProducer producer = new FanoutProducer(EXCHANGE_NAME);
        producer.start();

        final FanoutConsumer worker1 = new FanoutConsumer(EXCHANGE_NAME, "worker1");
        worker1.start();

        final FanoutConsumer worker2 = new FanoutConsumer(EXCHANGE_NAME, "worker2");
        worker2.start();

        producer.join();
        worker1.join();
        worker2.join();

        System.out.println("Done");
    }

}
