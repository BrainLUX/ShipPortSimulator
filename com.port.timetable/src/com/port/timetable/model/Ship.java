package com.port.timetable.model;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Ship implements Cloneable, Comparable<Ship> {
    private long arriveTime;
    private final String name;
    private final Type type;
    private final WeightType weightType;
    private final int weight;
    private long stayTime;
    private AtomicLong goods = new AtomicLong(0);
    private AtomicInteger workingCranes = new AtomicInteger(0);
    private AtomicInteger delay = new AtomicInteger(0);
    private long waitingTime = 0;
    private long workStartTime = 0;
    private long workLengthTime = 0;

    public Ship() {
        this(0, "", Type.LIQUID, 0);
    }

    public Ship(long arriveTime, String name, Type type, int weight) {
        this.arriveTime = arriveTime;
        this.name = name;
        this.type = type;
        this.weight = weight;
        switch (type) {
            case LOOSE:
                this.weightType = WeightType.TONS;
                goods.set(weight * 2);
                break;
            case LIQUID:
                this.weightType = WeightType.TONS;
                goods.set(weight / 2);
                break;
            default:
                this.weightType = WeightType.PIECES;
                goods.set(weight);
                break;
        }
        stayTime = (goods.get() / 2) * 1000 * 60;
    }

    public void setDelay(int delay) {
        this.delay.addAndGet(delay);
    }

    public AtomicInteger getDelay() {
        return delay;
    }

    public long getPenalty(long currentTime) {
        return Math.max(0, currentTime - arriveTime - stayTime) / 1000 / 60;
    }

    public AtomicInteger getWorkingCranes() {
        return workingCranes;
    }

    public AtomicLong getGoods() {
        return goods;
    }

    public Type getType() {
        return type;
    }

    public void setWaitingTime(long waitingTime) {
        this.waitingTime = waitingTime;
    }

    public long getWorkStartTime() {
        return workStartTime;
    }

    public void setWorkStartTime(long workStartTime) {
        this.workStartTime = workStartTime;
    }


    public void setWorkLengthTime(long workLengthTime) {
        this.workLengthTime = workLengthTime;
    }

    @Override
    public int compareTo(Ship o) {
        return Long.compare(arriveTime, o.arriveTime);
    }

    public enum Type {
        LOOSE("сыпучий"), LIQUID("жидкий"), CONTAINER("контейнер");
        private final String title;

        Type(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    private enum WeightType {
        TONS("тонн"), PIECES("контейнеров");
        private final String title;

        WeightType(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    public long getArriveTime() {
        return arriveTime;
    }

    public void updateArriveTime(long delay) {
        arriveTime += delay;
    }

    private String getTime(long time) {
        final Calendar format = Calendar.getInstance();
        format.setTimeInMillis(time);
        return String.format("%d-го %s в %d:%d",
                format.get(Calendar.DAY_OF_MONTH),
                format.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("ru")),
                format.get(Calendar.HOUR_OF_DAY),
                format.get(Calendar.MINUTE));
    }

    private String getWaitingFormatted() {
        String res = "";
        long minutes = waitingTime / 1000 / 60;
        long days = minutes / 60 / 24;
        res += days + ":";
        long hours = (minutes - days * 60 * 24) / 60;
        res += hours + ":";
        minutes = minutes - days * 60 * 24 - hours * 60;
        res += minutes;
        return res;
    }

    @Override
    public String toString() {
        if (waitingTime == 0) {
            return String.format("Корабль %s приплыл %s, имея на борту %d %s груза типа %s, будет разгружаться %d минут и стоять в порте на %d минут дольше",
                    this.name, this.getTime(this.arriveTime), this.weight, this.weightType.getTitle(), this.type.getTitle(), this.stayTime / 1000 / 60, this.delay.get());
        } else {
            return String.format("Корабль %s приплыл %s, ожидал в очереди %s, начал разружаться в %s в течении %d минут",
                    this.name, this.getTime(this.arriveTime), getWaitingFormatted(), this.getTime(this.workStartTime), this.workLengthTime / 1000 / 60);
        }
    }

    @Override
    public Ship clone() {
        final Ship ship = new Ship(this.arriveTime, this.name, this.type, this.weight);
        ship.setDelay(delay.get());
        return ship;
    }
}
