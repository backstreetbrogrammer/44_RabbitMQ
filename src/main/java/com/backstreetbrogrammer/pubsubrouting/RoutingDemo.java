package com.backstreetbrogrammer.pubsubrouting;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class RoutingDemo {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(final String[] args) throws InterruptedException {
        final RoutingConsumer worker1 = new RoutingConsumer(EXCHANGE_NAME,
                                                            "worker_all",
                                                            Arrays.asList("info", "warning", "error", "critical"));
        worker1.start();

        final RoutingConsumer worker2 = new RoutingConsumer(EXCHANGE_NAME,
                                                            "worker_err",
                                                            Collections.singletonList("error"));
        worker2.start();

        TimeUnit.MILLISECONDS.sleep(2000L);

        final RoutingProducer producer = new RoutingProducer(EXCHANGE_NAME);
        producer.start();

        producer.join();
        worker1.join();
        worker2.join();

        System.out.println("Done");
    }

}
