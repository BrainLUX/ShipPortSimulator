package com.dmitriev.oop.service;


import com.dmitriev.oop.entity.Ship;
import com.sun.istack.NotNull;

import java.util.LinkedList;

public interface TimetableService {
    @NotNull
    LinkedList<Ship> generate(long time);

    LinkedList<Ship> get();
}
