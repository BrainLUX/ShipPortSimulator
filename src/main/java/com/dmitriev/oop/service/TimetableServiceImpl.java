package com.dmitriev.oop.service;


import com.dmitriev.oop.entity.Ship;
import com.dmitriev.oop.exception.TimetableNotFoundException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.port.timetable.TimetableGenerator;
import com.sun.istack.NotNull;
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
    @NotNull
    public LinkedList<Ship> generate(long time) {
        ships.clear();
        ships.addAll(TimetableGenerator.generate(time));
        return get();
    }

    @Override
    @NotNull
    public LinkedList<Ship> get() {
        return ships;
    }

    @SuppressWarnings("deprecation")
    @Override
    @NotNull
    public Boolean add(@NotNull final Ship ship, @NotNull final String fileName) throws TimetableNotFoundException {
        final JsonParser jsonParser = new JsonParser();
        final JsonElement obj;
        final LinkedList<Ship> timetable = new LinkedList<>();
        try {
            obj = jsonParser.parse(new Scanner(new File(fileName)).nextLine());
            for (final JsonElement elem : obj.getAsJsonArray()) {
                final Ship sh = new Gson().fromJson(elem.toString(), Ship.class);
                timetable.add(sh);
            }
            timetable.add(ship);
            Collections.sort(timetable);
            try (final FileWriter fileWriter = new FileWriter(fileName)) {
                fileWriter.write(new Gson().toJson(timetable));
                fileWriter.flush();
            } catch (Exception e) {
                throw new TimetableNotFoundException();
            }
        } catch (FileNotFoundException e) {
            throw new TimetableNotFoundException();
        }
        return true;
    }
}
