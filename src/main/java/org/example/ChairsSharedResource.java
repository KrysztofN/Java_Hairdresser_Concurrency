package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChairsSharedResource {
    private int MAX_CHAIRS;
    private final ArrayList<ChairsEventListener> listeners = new ArrayList<>();
    private HashMap<Integer, String> chairCustomer = new HashMap<>();
    private HashMap<Integer, Long> chairEndTimes = new HashMap<>();

    public ChairsSharedResource(int MAX_CHAIRS) {
        this.MAX_CHAIRS = MAX_CHAIRS;
    }

    public synchronized void addChairsListener(final ChairsEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    public synchronized void removeChairsListener(final ChairsEventListener listener) {
        listeners.remove(listener);
    }

    public void notifyListeners() {
        for(ChairsEventListener listener : listeners) {
            listener.onChairsChanged(this);
        }
    }

    public synchronized int availableChairs(){
        return MAX_CHAIRS - chairCustomer.size();
    }

    public synchronized void releaseChair(String customer){
//        System.out.println("Customer " + customer + " has been served");

        Integer chairToRelease = null;
        for(Integer chairId : chairCustomer.keySet()) {
            if(chairCustomer.get(chairId).equals(customer)) {
                chairToRelease = chairId;
                break;
            }
        }
        if (chairToRelease != null) {
            chairCustomer.remove(chairToRelease);
            chairEndTimes.remove(chairToRelease);
//            System.out.println("Customer " + customer + " has left chair " + chairToRelease);
            notifyListeners();
        }
    }

    public synchronized void acquireChair(String customer, int serviceTime) {
//        System.out.println("Serving customer: " + customer);
        int chairId = 0;
        while(chairCustomer.containsKey(chairId)) {
            chairId++;
        }
        chairCustomer.put(chairId, customer);
        long endTime = System.currentTimeMillis() + serviceTime;
        chairEndTimes.put(chairId, endTime);
//        chairCapacity--;
//        System.out.println("Serving customer: " + customer + " in chair " + chairId);
        notifyListeners();
    }

    public synchronized boolean hasAvailableChairs() {
        return chairCustomer.size() < MAX_CHAIRS;
    }

    public HashMap<Integer, String> getChairsCopy() {
        return new HashMap<>(chairCustomer);
    }

    public synchronized HashMap<Integer, Integer> getRemainingTimes(){
        HashMap<Integer, Integer> remainingTimes = new HashMap<>();
        for (Map.Entry<Integer, Long> entry : chairEndTimes.entrySet()) {
            long remaining = entry.getValue() - System.currentTimeMillis();
            remainingTimes.put(entry.getKey(), remaining > 0 ? (int)(remaining / 1000) : 0);
        }
        return remainingTimes;
    }
}
