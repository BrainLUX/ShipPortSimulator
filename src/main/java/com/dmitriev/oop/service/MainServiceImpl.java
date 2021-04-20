package com.dmitriev.oop.service;


import com.dmitriev.oop.entity.StatisticObject;
import com.dmitriev.oop.exception.TimetableNotFoundException;
import com.google.gson.Gson;
import com.port.port.PortController;
import com.port.timetable.TimetableGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

@Service
public class MainServiceImpl implements MainService {
    private RestTemplate restTemplate = new RestTemplate();
    private String url = "http://localhost:8080/timetable/generate/";

    @Override
    public String getTimetable() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String fileName = "timetable-" + UUID.randomUUID().toString().substring(0, TimetableGenerator.UUID_LENGTH) + ".json";
        try {
            final FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(Objects.requireNonNull(responseEntity.getBody()));
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception ignored) {
        }
        return fileName;
    }

    @Override
    public String getTimetableByFile(String fileName) {
        StringBuilder result = new StringBuilder();
        File file = new File(fileName);
        if (file.exists()) {
            try {
                Scanner sc = new Scanner(file);
                while (sc.hasNext()) {
                    result.append(sc.nextLine());
                }
                return result.toString();
            } catch (Exception e) {
                throw new TimetableNotFoundException();
            }
        }
        throw new TimetableNotFoundException();
    }

    @Override
    public String saveStatistic(String statisticObject) {
        String fileName = "statistic-" + UUID.randomUUID().toString().substring(0, TimetableGenerator.UUID_LENGTH) + ".json";
        try {
            final FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(statisticObject);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception ignored) {
        }
        return fileName;
    }
}
