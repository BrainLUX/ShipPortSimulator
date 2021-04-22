package com.dmitriev.oop.service;

import com.sun.istack.NotNull;

public interface MainService {
    @NotNull
    String getTimetable();

    @NotNull
    String getTimetableByFile(@NotNull final String fileName);

    @NotNull
    String saveStatistic(@NotNull final String object);
}
