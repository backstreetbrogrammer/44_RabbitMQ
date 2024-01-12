package com.backstreetbrogrammer.pubsubtopics;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class TopicsDemo {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(final String[] args) throws InterruptedException {
        final TopicsConsumer worker1 = new TopicsConsumer(EXCHANGE_NAME,
                                                          "worker_all_errors",
                                                          Collections.singletonList("*.logs.error"));
        worker1.start();

        final TopicsConsumer worker2 = new TopicsConsumer(EXCHANGE_NAME,
                                                          "worker_app1",
                                                          Collections.singletonList("application1.#"));
        worker2.start();

        TimeUnit.MILLISECONDS.sleep(2000L);

        final TopicsProducer producer = new TopicsProducer(EXCHANGE_NAME);
        producer.start();

        producer.join();
        worker1.join();
        worker2.join();

        System.out.println("Done");
    }

}
