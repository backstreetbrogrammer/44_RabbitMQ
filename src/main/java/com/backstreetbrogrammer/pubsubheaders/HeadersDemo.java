package com.backstreetbrogrammer.pubsubheaders;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class HeadersDemo {

    private static final String EXCHANGE_NAME = "header_logs_exchange";

    public static void main(final String[] args) throws InterruptedException {
        final HeadersConsumer worker1 = new HeadersConsumer(EXCHANGE_NAME,
                                                            "worker_errors",
                                                            Collections.singletonList("error"));
        worker1.start();

        final HeadersConsumer worker2 = new HeadersConsumer(EXCHANGE_NAME,
                                                            "worker_info_warn",
                                                            Arrays.asList("info", "warn"));
        worker2.start();

        TimeUnit.MILLISECONDS.sleep(2000L);

        final HeadersProducer producer = new HeadersProducer(EXCHANGE_NAME);
        producer.start();

        producer.join();
        worker1.join();
        worker2.join();

        System.out.println("Done");
    }
}
