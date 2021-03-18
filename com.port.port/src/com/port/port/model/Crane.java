package com.port.port.model;

import com.port.timetable.model.Ship;

public class Crane extends Thread {
    private Ship ship;
    private volatile boolean running = true;

    public Crane() {
        ship = null;
        this.start();
    }

    public boolean isEnded() {
        return ship == null;
    }

    @Override
    public void run() {
        super.run();
        while (running) {
            if (ship != null) {
                if (ship.getGoods().updateAndGet(n -> (n > 0) ? n - 1 : n) == 0 && ship.getDelay().get() == 0) {
                    setShip(null);
                }
            }
            synchronized (this) {
                if (!this.isInterrupted()) {
                    try {
                        wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public void terminate() {
        running = false;
        interrupt();
    }
}
