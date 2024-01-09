package com.backstreetbrogrammer.workerqueue;

public class WorkerQueueDemo {

    private final static String QUEUE_NAME = "worker_queue";

    public static void main(final String[] args) throws InterruptedException {
        final WorkerQueueProducer producer = new WorkerQueueProducer(QUEUE_NAME);
        producer.start();

        final WorkerQueueConsumer worker1 = new WorkerQueueConsumer(QUEUE_NAME, "worker1");
        worker1.start();

        final WorkerQueueConsumer worker2 = new WorkerQueueConsumer(QUEUE_NAME, "worker2");
        worker2.start();

        producer.join();
        worker1.join();
        worker2.join();

        System.out.println("Done");
    }

}
