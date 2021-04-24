package com.port.controller;

import com.dmitriev.oop.entity.Ship;
import com.dmitriev.oop.entity.StatisticObject;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.port.controller.model.Operator;
import com.port.controller.utils.CommandController;
import com.port.controller.utils.ConsoleHandler;
import com.port.port.PortController;
import com.port.timetable.TimetableGenerator;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.UnaryOperator;

import static com.port.controller.utils.ConsoleHandler.ErrorType.*;
import static com.port.controller.utils.ConsoleHandler.MessageType.*;

public class Program {

    public static RestTemplate restTemplate = new RestTemplate();
    public static String url = "http://localhost:8080/main/";
    private static String addShipUrl = "http://localhost:8080/timetable/add";

    private static String timetableFile = "";
    private static String statisticFile = "";
    private final static Scanner scanner = new Scanner(System.in);
    private final static long time = System.currentTimeMillis();
    private static CommandController controller;

    public static void start() {
        final Operator<String> handleUserInput = scanner::nextLine;
        controller = new CommandController(handleUserInput, getCommandsHandlers());
        controller.start();
    }

    @NotNull
    private static Operator[] getCommandsHandlers() {
        final Operator<Void> generate = Program::generateNewTimetable;
        final Operator<Void> show = Program::showTimetable;
        final Operator<Void> add = Program::addNewShip;
        final Operator<Void> start = Program::startSimulation;
        final Operator<Void> stats = Program::showStatistic;
        final Operator<Void> quit = Program::quit;
        return new Operator[]{generate, show, add, start, stats, quit};
    }

    @Nullable
    private static Void generateNewTimetable() {
        ConsoleHandler.printMessage(TIMETABLE_GENERATE_START);
        final ResponseEntity<String> responseEntity = restTemplate.getForEntity(url + "timetable/", String.class);
        timetableFile = responseEntity.getBody();
        ConsoleHandler.printMessage(TIMETABLE_GENERATE_END);
        return null;
    }

    @SuppressWarnings("deprecation")
    @Nullable
    private static Void showTimetable() {
        System.out.println("Вывод расписания:\n");
        if (timetableFile.isBlank()) {
            ConsoleHandler.printError(NO_TIMETABLE);
        }
        final ResponseEntity<String> responseEntity = restTemplate.getForEntity(url + "timetable/" + timetableFile, String.class);
        final LinkedList<Ship> timetable = new LinkedList<>();
        final JsonParser jsonParser = new JsonParser();
        final JsonElement obj = jsonParser.parse(Objects.requireNonNull(responseEntity.getBody()));
        for (final JsonElement elem : obj.getAsJsonArray()) {
            timetable.add(new Gson().fromJson(elem.toString(), Ship.class));
        }
        Collections.sort(timetable);
        timetable.forEach(System.out::println);
        return null;
    }

    @Nullable
    private static Void addNewShip() {
        System.out.println("Добавление нового корабля (оставьте поле пустым для случайного значения");
        System.out.println("Введите дату прибытия в формате дд.мм.год чч:мм: ");
        final String date = scanner.nextLine();
        System.out.println("Выберите тип корабля (1. Сыпучий, 2. Жидкий, 3. Контейнер):");
        final String type = scanner.nextLine();
        System.out.println("Введите вес: ");
        final String weight = scanner.nextLine();
        System.out.println("Введите задержку: ");
        final String delay = scanner.nextLine();
        try {
            final Ship ship = TimetableGenerator.generateShip(time, date, type, weight, delay);
            System.out.println("Информация о добавленном корабле: " + ship);
            restTemplate.postForEntity(addShipUrl + "?fileName=" + timetableFile, ship, String.class);
            ConsoleHandler.printMessage(SHIP_ADDED);
        } catch (Exception e) {
            ConsoleHandler.printError(BAD_INPUT);
        }

        return null;
    }

    @Nullable
    private static Void startSimulation() {
        if (timetableFile.isBlank()) {
            ConsoleHandler.printError(NO_TIMETABLE);
        } else {
            controller.end();
            final UnaryOperator<String> onEnd = statisticObject -> {
                ConsoleHandler.printMessage(SIMULATION_END);
                statisticFile = statisticObject;
                controller.start();
                return null;
            };
            ConsoleHandler.printMessage(SIMULATION_START);
            ConsoleHandler.setCyanColor();
            final PortController portController = new PortController(time, timetableFile, onEnd);
            portController.initPort();
        }
        return null;
    }

    @Nullable
    private static Void showStatistic() {
        if (!statisticFile.isBlank()) {
            final StatisticObject statisticObject;
            final StringBuilder result = new StringBuilder();
            final File file = new File(statisticFile);
            try {
                final Scanner sc = new Scanner(file);
                while (sc.hasNext()) {
                    result.append(sc.nextLine());
                }
            } catch (Exception e) {
                ConsoleHandler.printError(BAD_INPUT);
            }
            statisticObject = new Gson().fromJson(result.toString(), StatisticObject.class);
            System.out.println(statisticObject);
            if (ConsoleHandler.askUser(PRINT_SHIPS, scanner)) {
                statisticObject.printShipStat();
            }
        } else {
            ConsoleHandler.printError(NO_SIMULATIONS);
        }
        return null;
    }

    @Nullable
    private static Void quit() {
        if (ConsoleHandler.askUser(QUIT, scanner)) {
            controller.end();
        }
        return null;
    }

}
