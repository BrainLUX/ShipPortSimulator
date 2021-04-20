package com.dmitriev.oop.service;

public interface MainService {
    String getTimetable();

    String getTimetableByFile(String fileName);

    String saveStatistic(String object);
}
