package com.port.port.model;

import org.jetbrains.annotations.Nullable;
import com.port.timetable.model.Ship;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Crane extends Thread {
    private Ship ship;
    private boolean running;
    private final Lock lock = new ReentrantLock();
    private final Condition isWorking = lock.newCondition();

    public Crane() {
        ship = null;
        running = true;
        setPriority(MAX_PRIORITY);
        start();
    }

    @Override
    public void run() {
        super.run();
        while (running) {
            lock.lock();
            work();
            try {
                isWorking.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();
        }
    }

    public boolean isEnded() {
        return ship == null;
    }

    private void work() {
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

    public void startWorking() {
        lock.lock();
        isWorking.signal();
        lock.unlock();
    }
}
