package com.port.timetable;

import com.port.timetable.model.Ship;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;

public class TimetableGenerator {
    private static final long PERIOD_TIME = 30 * 1000 * 60 * 60 * 24L;
    private static final int MAX_SHIPS = 200;
    private static final int MAX_WEIGHT = 1000;
    private static final int MAX_WEIGHT_DELAY = 1440;
    public static final int UUID_LENGTH = 5;
    private static final long MAX_TIME_DELAY = 7 * 1000 * 60 * 60 * 24L;
    private static final Random random = new Random();

    public static LinkedList<Ship> generate(long time) {
        final LinkedList<Ship> timetable = new LinkedList<>();
        final int shipCount = random.nextInt(MAX_SHIPS);
        for (int i = 0; i < shipCount; i++) {
            //TODO UUID заменить
            final Ship ship = new Ship(time + Math.floorMod(random.nextLong(), PERIOD_TIME),
                    UUID.randomUUID().toString().substring(0, UUID_LENGTH), getRandomShipType(), random.nextInt(MAX_WEIGHT));
            timetable.add(ship);
        }
        return randomValues(timetable, time);
    }

    private static LinkedList<Ship> randomValues(LinkedList<Ship> timetable, long time) {
        timetable.forEach(ship -> {
            int d = random.nextInt(MAX_WEIGHT_DELAY);
            ship.setDelay(d);
            final long delay = -MAX_TIME_DELAY + random.nextLong() % (MAX_TIME_DELAY * 2);
            if (ship.getArriveTime() + delay < time) {
                ship.updateArriveTime(-delay);
            } else {
                ship.updateArriveTime(delay);
            }
        });
        Collections.sort(timetable);
        timetable.forEach(System.out::println);
        return timetable;
    }

    private static Ship.Type getRandomShipType() {
        return Ship.Type.values()[random.nextInt(Ship.Type.values().length)];
    }
}
