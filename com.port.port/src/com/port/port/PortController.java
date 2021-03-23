package com.port.port;

import com.port.port.model.Crane;
import com.port.port.model.StatisticObject;
import com.port.timetable.model.Ship;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import java.util.spi.AbstractResourceBundleProvider;

public class PortController {

    private final LinkedList<Ship> timetable;
    private final AtomicLong currentTime;
    private AtomicLong minPenalty = new AtomicLong(Long.MAX_VALUE);
    private int[] minCranesCount;
    private long avgQueryLength = 0;
    private long avgQueryDuration = 0;
    private int avgDelay = 0;
    private int maxDelay = Integer.MIN_VALUE;
    private long minTime = 0;
    private ArrayList<Ship> lastDoneList = new ArrayList<>();
    private final Calendar calendar = Calendar.getInstance();
    private final UnaryOperator<StatisticObject> onEnd;

    public PortController(LinkedList<Ship> timetable, long startTime, UnaryOperator<StatisticObject> onEnd) {
        this.timetable = timetable;
        currentTime = new AtomicLong(startTime);
        timetable.forEach(ship -> {
            if (ship.getDelay().get() > maxDelay) {
                maxDelay = ship.getDelay().get();
            }
            avgDelay += ship.getDelay().get();
        });
        calendar.setTimeInMillis(startTime);
        this.onEnd = onEnd;
    }

    public void initPort() {
        int[] cranesCount = new int[]{2, 2, 2};
        simulate(cranesCount, 0, 0);
    }

    private void simulate(int[] cranesCount, int stage, int noChanges) {
        final Ship.Type[] types = Ship.Type.values();
        if (stage == 3) {
            StatisticObject statisticObject = new StatisticObject(calendar.getTimeInMillis(), minTime, avgQueryLength,
                    avgQueryDuration, avgDelay, maxDelay, minPenalty.get(), minCranesCount, lastDoneList);
            System.out.println();
            onEnd.apply(statisticObject);
            return;
        }
        AtomicLong currentTime = new AtomicLong(this.currentTime.get());
        LinkedList<Ship> timetable = new LinkedList<>();
        this.timetable.forEach(ship ->
                timetable.add(ship.clone()));
        final ConcurrentHashMap<Ship.Type, Queue<Ship>> waitingShip = new ConcurrentHashMap<>();
        final ConcurrentHashMap<Ship.Type, Queue<Ship>> performShip = new ConcurrentHashMap<>();
        final HashMap<Ship.Type, ArrayList<Crane>> cranes = new HashMap<>();

        for (Ship.Type value : types) {
            final ArrayList<Crane> list = new ArrayList<>();
            for (int i = 0; i < cranesCount[value.ordinal()]; i++) {
                list.add(new Crane());
            }
            cranes.put(value, list);
            waitingShip.put(value, new ConcurrentLinkedQueue<>());
            performShip.put(value, new ConcurrentLinkedQueue<>());
        }
        AtomicLong queryLength = new AtomicLong();
        AtomicLong queryCount = new AtomicLong();
        AtomicLong queryDuration = new AtomicLong();
        AtomicLong queryDurationCount = new AtomicLong();
        long count = timetable.size();
        AtomicLong processed = new AtomicLong(0);
        final AtomicLong penalty = new AtomicLong(0);
        new Thread(() -> {
            final ArrayList<Ship> doneList = new ArrayList<>();
            while (true) {
                AtomicReference<Iterator<Ship>> iterator = new AtomicReference<>(timetable.iterator());
                while (iterator.get().hasNext()) {
                    Ship ship = iterator.get().next();
                    if (ship.getArriveTime() <= currentTime.get()) {
                        waitingShip.get(ship.getType()).add(ship);
                        iterator.get().remove();
                    } else {
                        break;
                    }
                }
                performShip.forEach((type, queue) -> {
                    iterator.set(queue.iterator());
                    while (iterator.get().hasNext()) {
                        Ship ship = iterator.get().next();
                        if (ship.getGoods().get() == 0) {
                            if (ship.getDelay().get() == 0) {
                                penalty.addAndGet(ship.getPenalty(currentTime.get()));
                                processed.incrementAndGet();
                                ship.setWorkLengthTime(currentTime.get() - ship.getWorkStartTime());
                                doneList.add(ship);
                                iterator.get().remove();
                            } else {
                                ship.getDelay().decrementAndGet();
                            }
                        }
                    }
                });
                cranes.forEach((type, craneList) -> craneList.forEach(crane -> {
                    if (crane.isEnded()) {
                        performShip.get(type).forEach(ship -> {
                            if (crane.isEnded() && ship.getWorkingCranes().get() == 1) {
                                ship.getWorkingCranes().incrementAndGet();
                                crane.setShip(ship);
                            }
                        });
                        if (crane.isEnded()) {
                            final Ship ship = waitingShip.get(type).poll();
                            if (ship != null) {
                                ship.getWorkingCranes().incrementAndGet();
                                crane.setShip(ship);
                                ship.setWaitingTime(currentTime.get() - ship.getArriveTime());
                                ship.setWorkStartTime(currentTime.get());
                                performShip.get(type).add(ship);
                                queryDuration.addAndGet(currentTime.get() - ship.getArriveTime());
                                queryDurationCount.incrementAndGet();
                            }
                        }
                    }
                    synchronized (crane) {
                        crane.notify();
                    }
                }));
                waitingShip.forEach(((type, ships) ->
                {
                    if (ships.size() > 0) {
                        queryLength.addAndGet(ships.size());
                        queryCount.incrementAndGet();
                    }
                }));
                if (processed.get() == count) {
                    cranes.forEach((type, craneList) -> craneList.forEach(Crane::terminate));
                    break;
                }
                currentTime.addAndGet(1000 * 60);
            }
            long finalPenalty = penalty.get() / 60 * 100;
            System.out.print("█");
            if (minPenalty.get() > finalPenalty) {
                if (minPenalty.get() - finalPenalty < 30000) {
                    if (noChanges >= 3) {
                        cranesCount[stage] -= noChanges;
                        simulate(cranesCount, stage + 1, 0);
                    } else {
                        cranesCount[stage] += 1;
                        simulate(cranesCount, stage, noChanges + 1);
                    }
                } else {
                    cranesCount[stage] += 1;
                    if (queryCount.get() > 0) {
                        avgQueryLength = queryLength.get() / queryCount.get();
                        avgQueryDuration = queryDuration.get() / queryDurationCount.get();
                    }
                    minTime = currentTime.get();
                    minPenalty.set(finalPenalty);
                    minCranesCount = cranesCount;
                    lastDoneList.clear();
                    lastDoneList.addAll(doneList);
                    simulate(cranesCount, stage, 0);
                }
            } else if (noChanges >= 3) {
                cranesCount[stage] -= noChanges;
                simulate(cranesCount, stage + 1, 0);
            } else {
                cranesCount[stage] += 1;
                simulate(cranesCount, stage, noChanges + 1);
            }
        }).start();
    }
}
