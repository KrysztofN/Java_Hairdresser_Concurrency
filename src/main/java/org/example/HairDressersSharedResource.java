package org.example;

import java.util.ArrayList;
import java.util.HashMap;

public class HairDressersSharedResource {
    private int hairdressersS;
    private int hairdressersM;
    private int hairdressersG;
    private final ArrayList<HairdressersEventListener> listeners = new ArrayList<>();

    public HairDressersSharedResource(int[] LISTA_FRYZJEROW) {
        hairdressersS = LISTA_FRYZJEROW[0];
        hairdressersM = LISTA_FRYZJEROW[1];
        hairdressersG = LISTA_FRYZJEROW[2];
    }

    public synchronized void addHairdressersListener(final HairdressersEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    public synchronized void removeHairdressersListener(final HairdressersEventListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for(HairdressersEventListener listener : listeners) {
            listener.onHairDresserChange(this);
        }
    }

    public synchronized void decrementHairdressers(String hairdresser) {
        switch (hairdresser) {
            case "S" -> hairdressersS--;
            case "M" -> hairdressersM--;
            case "G" -> hairdressersG--;
        }
        notifyListeners();
    }

    public synchronized void incrementHairdressers(String hairdresser) {
        switch (hairdresser) {
            case "S" -> hairdressersS++;
            case "M" -> hairdressersM++;
            case "G" -> hairdressersG++;
        }
        notifyListeners();
    }

    public synchronized boolean hasAvailableHairdressers(String hairdresser) {
        switch (hairdresser) {
            case "S" -> {
                return hairdressersS > 0;
            }
            case "M" -> {
                return hairdressersM > 0;
            }
            case "G" -> {
                return hairdressersG > 0;
            }
        }
        return false;
    }

    public synchronized int availableHairdressers(String hairdresser) {
        switch (hairdresser) {
            case "S" -> {
                return hairdressersS;
            }
            case "M" -> {
                return hairdressersM;
            }
            case "G" -> {
                return hairdressersG;
            }
        }
        return 0;
    }
}
