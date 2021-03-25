package com.port.port.model;

import org.jetbrains.annotations.Nullable;
import com.port.timetable.model.Ship;

public class Crane {
    private volatile Ship ship;
    private volatile boolean running = false;
    private volatile boolean work = false;
    public Thread executor;

    public Crane() {
        ship = null;
        running = true;
    }

    public boolean isEnded() {
        return ship == null;
    }

    public void work(){
        if (ship != null) {
            if (ship.getGoods().updateAndGet(n -> (n > 0) ? n - 1 : n) == 0 && ship.getDelay().get() == 0) {
                setShip(null);
            }
        }
    }

    public void work2() {
        if (ship != null) {
            executor = new Thread(() -> {
                //System.out.println("crane");
                if (ship != null) {
                    if (ship.getGoods().updateAndGet(n -> (n > 0) ? n - 1 : n) == 0 && ship.getDelay().get() == 0) {
                        setShip(null);
                    }
                }
            });
            executor.start();
        }
//        if (ship != null) {
//            if (ship.getGoods().updateAndGet(n -> (n > 0) ? n - 1 : n) == 0 && ship.getDelay().get() == 0) {
//                setShip(null);
//            }
//        }
    }

    public void setShip(@Nullable final Ship ship) {
        this.ship = ship;
    }

    public void terminate() {
        running = false;
        // executor.interrupt();
    }
}
