package com.dmitriev.oop.service;


import com.dmitriev.oop.entity.Ship;
import com.sun.istack.NotNull;

import java.util.LinkedList;
import java.util.List;

public interface TimetableService {
    @NotNull
    LinkedList<Ship> generate(long time);

    LinkedList<Ship> get();

    Boolean add(Ship ship, String fileName);
}
