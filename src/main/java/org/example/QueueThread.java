package org.example;

import java.util.Random;

public class QueueThread extends Thread {
    public QueueSharedResource queue;
    private final String[] options = {"S", "M", "G"};
    private volatile boolean running = true;
    private final counter counterS = new counter();
    private final counter counterM = new counter();
    private final counter counterG = new counter();
    private counter[] counters;

    public QueueThread(QueueSharedResource queue) {
        this.queue = queue;
        this.counters = new counter[]{counterS, counterM, counterG};
    }

    @Override
    public void run() {
        Random r = new Random();
        while(running) {
            int sleepTime = r.nextInt(1000);
            int option = r.nextInt(3);
            try {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                if (queue.isFull()) {
//                    System.out.println("Queue full");
                } else{
                    try {
                        String customer = counters[option].getCounter();
                        queue.enqueue(options[option] + customer);
                        counters[option].increment();

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
