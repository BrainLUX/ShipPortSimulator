package com.port.port.model;

import com.port.timetable.model.Ship;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class StatisticObject {
    private final long startTime;
    private final long endTime;
    private final long avgQueryLength;
    private final long avgQueryDuration;
    private final long avgDelay;
    private final long maxDelay;
    private final long penalty;
    private final int[] minCranesCount;
    private final ArrayList<Ship> shipList;

    public StatisticObject(long startTime, long endTime, long avgQueryLength, long avgQueryDuration, long avgDelay, long maxDelay, long penalty, int[] minCranesCount, ArrayList<Ship> shipList) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.avgQueryLength = avgQueryLength;
        this.avgQueryDuration = avgQueryDuration;
        this.avgDelay = avgDelay;
        this.maxDelay = maxDelay;
        this.penalty = penalty;
        this.minCranesCount = minCranesCount;
        this.shipList = shipList;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        System.out.println();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);
        result.append("Время начала разгрузки ").append(String.format("%d-го %s в %d:%d",
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("ru")),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE))).append("\n");
        calendar.setTimeInMillis(endTime);
        result.append("Время окончания разгрузки ").append(String.format("%d-го %s в %d:%d",
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("ru")),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE))).append("\n");
        result.append("Число разгруженных судов: ").append(shipList.size()).append("\n");
        result.append("Средняя длина очереди на разгрузку: ").append(avgQueryLength).append(" кораблей").append("\n");
        result.append("Среднее время ожидания в очереди: ").append(avgQueryDuration / 1000 / 60).append(" минут").append("\n");
        result.append("Максимальная задержка разгрузки: ").append(maxDelay).append(" минут").append("\n");
        result.append("Средняя задержка разгрузки: ").append(avgDelay / shipList.size()).append(" минут").append("\n");
        result.append("Общая сумма штрафа : ").append(penalty).append(" у. е.").append("\n");
        result.append("Итоговое необходимое количество кранов каждого вида:").append("\n");
        for (Ship.Type value : Ship.Type.values()) {
            result.append(value.getTitle()).append(": ").append(minCranesCount[value.ordinal()]).append("\n");
        }
        result.append("Информация о кораблях").append("\n");
        for (Ship ship : shipList) {
            result.append(ship).append("\n");
        }
        return result.toString();
    }
}
