package com.port.port.model;

import org.jetbrains.annotations.Nullable;
import com.port.timetable.model.Ship;

public class Crane extends Thread {
    private volatile Ship ship;
    private volatile boolean running;
    public final Object mutex = new Object();

    public Crane() {
        ship = null;
        running = true;
        setPriority(MAX_PRIORITY);
        this.start();
    }

    @Override
    public void run() {
        super.run();
        while (running) {
            synchronized (mutex) {
                work();
                try {
                    mutex.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isEnded() {
        return ship == null;
    }

    private void work() {
       // System.out.println("work");
        if (ship != null) {
            if (ship.getGoods().updateAndGet(n -> (n > 0) ? n - 1 : n) == 0 && ship.getDelay().get() == 0) {
                setShip(null);
            }
        }
    }

    public void setShip(@Nullable final Ship ship) {
        this.ship = ship;
    }

    public void terminate() {
        running = false;
    }
}
