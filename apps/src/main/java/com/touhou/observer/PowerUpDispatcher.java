package com.touhou.observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PowerUpDispatcher {
    private final List<PowerUpObserver> observers;

    public PowerUpDispatcher() {
        observers = new CopyOnWriteArrayList<>();
    }

    public void addObserver(PowerUpObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(PowerUpObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(PowerUpEvent event) {
        for (PowerUpObserver observer : observers) {
            observer.onPowerUpActivated(event);
        }
    }
}
