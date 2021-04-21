package com.dmitriev.oop.service;


import com.dmitriev.oop.entity.Ship;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.port.timetable.TimetableGenerator;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

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

    @SuppressWarnings("deprecation")
    @Override
    public Boolean add(Ship ship, String fileName) {
        JsonParser jsonParser = new JsonParser();
        JsonElement obj;
        LinkedList<Ship> timetable = new LinkedList<>();
        try {
            obj = jsonParser.parse(new Scanner(new File(fileName)).nextLine());
            for (JsonElement elem : obj.getAsJsonArray()) {
                Ship sh = new Gson().fromJson(elem.toString(), Ship.class);
                timetable.add(sh);
            }
            timetable.add(ship);
            Collections.sort(timetable);
            try {
                final FileWriter fileWriter = new FileWriter(fileName);
                fileWriter.write(new Gson().toJson(timetable));
                fileWriter.flush();
                fileWriter.close();
            } catch (Exception ignored) {
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }
}
