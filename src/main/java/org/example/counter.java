package org.example;

import java.util.concurrent.atomic.AtomicInteger;

public class counter {
    private AtomicInteger counter = new AtomicInteger(1);

    public String getCounter(){
        return String.valueOf(counter.get());
    }

    public void increment(){
        counter.incrementAndGet();
    }

}
