package com.dmitriev.oop.service;


import com.dmitriev.oop.entity.Ship;
import com.sun.istack.NotNull;

import java.util.LinkedList;
import java.util.List;

public interface TimetableService {
    @NotNull
    LinkedList<Ship> generate(final long time);

    @NotNull
    LinkedList<Ship> get();

    @NotNull
    Boolean add(@NotNull final Ship ship, @NotNull final String fileName);
}
