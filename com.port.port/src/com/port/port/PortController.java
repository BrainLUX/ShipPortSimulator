package com.port.port;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.port.port.model.Crane;
import com.port.port.model.StatisticObject;
import com.port.timetable.model.Ship;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

import static com.port.timetable.TimetableGenerator.CRANE_COST;
import static com.port.timetable.TimetableGenerator.MINUTE;

public class PortController {

    public static final String TIMETABLE_FILE = "timetable.json";
    private final LinkedList<Ship> timetable = new LinkedList<>();
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
    private StatisticObject statisticObject;

    @SuppressWarnings("deprecation")
    public PortController(final long startTime, @NotNull final UnaryOperator<StatisticObject> onEnd) throws FileNotFoundException {
        JsonParser jsonParser = new JsonParser();
        JsonElement obj = jsonParser.parse(new Scanner(new File(TIMETABLE_FILE)).nextLine());
        for (JsonElement elem : obj.getAsJsonArray()) {
            this.timetable.add(new Gson().fromJson(elem.toString(), Ship.class));
        }
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
        final int[] cranesCount = new int[]{1, 1, 1};
        simulate(cranesCount, 0, 0);
        onEnd.apply(statisticObject);
    }

    private void simulate(@NotNull final int[] cranesCount, final int stage, final int noChanges) {
        final Ship.Type[] types = Ship.Type.values();
        if (stage == 3) {
            statisticObject = new StatisticObject(calendar.getTimeInMillis(), minTime, avgQueryLength,
                    avgQueryDuration, avgDelay, maxDelay, minPenalty.get(), minCranesCount, lastDoneList);
            System.out.println();
            return;
        }
        final AtomicLong currentTime = new AtomicLong(this.currentTime.get());
        final LinkedList<Ship> timetable = new LinkedList<>();
        this.timetable.forEach(ship ->
                timetable.add(ship.clone()));
        final HashMap<Ship.Type, Queue<Ship>> waitingShip = new HashMap<>();
        final HashMap<Ship.Type, Queue<Ship>> performShip = new HashMap<>();
        final HashMap<Ship.Type, ArrayList<Crane>> cranes = new HashMap<>();

        for (final Ship.Type value : types) {
            final ArrayList<Crane> list = new ArrayList<>();
            for (int i = 0; i < cranesCount[value.ordinal()]; i++) {
                list.add(new Crane());
            }
            cranes.put(value, list);
            waitingShip.put(value, new LinkedList<>());
            performShip.put(value, new LinkedList<>());
        }
        final AtomicLong queryLength = new AtomicLong();
        final AtomicLong queryCount = new AtomicLong();
        final AtomicLong queryDuration = new AtomicLong();
        final AtomicLong queryDurationCount = new AtomicLong();
        final AtomicLong processed = new AtomicLong(0);
        final AtomicLong penalty = new AtomicLong(0);
        final ArrayList<Ship> doneList = new ArrayList<>();
        final AtomicReference<Iterator<Ship>> iterator = new AtomicReference<>();
        while (true) {
            iterator.set(timetable.iterator());
            while (iterator.get().hasNext()) {
                final Ship ship = iterator.get().next();
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
                    final Ship ship = iterator.get().next();
                    if (ship.getGoods().get() == 0) {
                        if (ship.getDelay().updateAndGet(n -> (n > 0) ? n - 1 : n) == 0) {
                            penalty.addAndGet(ship.getPenalty(currentTime.get()));
                            processed.incrementAndGet();
                            ship.setWorkLengthTime(currentTime.get() - ship.getWorkStartTime());
                            doneList.add(ship);
                            iterator.get().remove();
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
                crane.startWorking();
            }));
            waitingShip.forEach(((type, ships) ->
            {
                if (ships.size() > 0) {
                    queryLength.addAndGet(ships.size());
                    queryCount.incrementAndGet();
                }
            }));
            if (processed.get() == this.timetable.size()) {
                cranes.forEach((type, craneList) -> craneList.forEach(Crane::terminate));
                break;
            }
            currentTime.addAndGet(MINUTE);
        }
        final long finalPenalty = penalty.get() / 60 * 100;
        System.out.print("█");

        if (minPenalty.get() > finalPenalty) {
            if (minPenalty.get() - finalPenalty < CRANE_COST) {
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
    }
}
