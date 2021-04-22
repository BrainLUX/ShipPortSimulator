package com.dmitriev.oop.service;

import com.dmitriev.oop.exception.TimetableNotFoundException;
import com.port.timetable.TimetableGenerator;
import com.sun.istack.NotNull;
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
    private final RestTemplate restTemplate = new RestTemplate();
    private final String url = "http://localhost:8080/timetable/generate/";

    @Override
    @NotNull
    public String getTimetable() {
        final ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        final String fileName = "timetable-" + UUID.randomUUID().toString().substring(0, TimetableGenerator.UUID_LENGTH) + ".json";
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
    @NotNull
    public String getTimetableByFile(@NotNull final String fileName) {
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
    @NotNull
    public String saveStatistic(@NotNull final String statisticObject) {
        final String fileName = "statistic-" + UUID.randomUUID().toString().substring(0, TimetableGenerator.UUID_LENGTH) + ".json";
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
