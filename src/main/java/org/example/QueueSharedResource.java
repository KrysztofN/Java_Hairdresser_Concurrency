package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class QueueSharedResource {
    private final Queue<String> queue = new LinkedList<String>();
    private final int MAX_CAPACITY;
    private final ArrayList<QueueEventListener> listeners = new ArrayList<>();

    public QueueSharedResource(int MAX_CAPACITY) {
        this.MAX_CAPACITY = MAX_CAPACITY;
    }

    public synchronized void addQueueListener(final QueueEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    public synchronized void removeQueueListener(final QueueEventListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for(QueueEventListener listener : listeners) {
            listener.onQueueChanged(this);
        }
    }

    public synchronized void enqueue(String item) throws InterruptedException {
        while(isFull()){
//            System.out.println("Queue full");
            wait();
        }
        queue.add(item);

//        System.out.println("Enqueued " + item + " People in queue: " + queue.size());
        notifyListeners();
        notifyAll();
    }

    public synchronized boolean isFull(){
        if(queue.size() < MAX_CAPACITY) {
            return false;
        } return true;
    }

    public synchronized String dequeue() throws InterruptedException {
        while(queue.isEmpty()) {
            wait();
        }

        String item =  queue.poll();
//        System.out.println("People in queue: " + queue.size() + " Dequeued " + item);
        notifyListeners();
        notifyAll();
        return item;

    }

    public synchronized String peek() {
        if(queue.isEmpty()) {
            return null;
        } else{
            return queue.peek();
        }
    }

    public synchronized int size() {
        return queue.size();
    }

    public synchronized String returnFirstOccurence(HashMap<String, Boolean> hairdressersAvailable) {
        for(String customer : queue){
            if(hairdressersAvailable.get(customer.substring(0, 1))) return customer;
        }
        return "";
    }

    public synchronized void removeFromFifo(String customer) throws InterruptedException {
        queue.remove(customer);
        notifyListeners();
    }

    public synchronized Queue<String> getQueueCopy(){
        return new LinkedList<>(queue);
    }
}
