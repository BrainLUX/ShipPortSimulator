package com.dmitriev.oop.service;


import com.dmitriev.oop.entity.Ship;
import com.port.timetable.TimetableGenerator;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class TimetableServiceImpl implements TimetableService {

    private LinkedList<Ship> ships = new LinkedList<>();

    @Override
    public LinkedList<Ship> generate(long time) {
        ships.clear();
        ships.addAll(TimetableGenerator.generate(time));
        return get();
    }

    @Override
    public LinkedList<Ship> get() {
        return ships;
    }
}
