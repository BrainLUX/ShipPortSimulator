package com.port.controller;

import com.google.gson.Gson;
import com.port.controller.model.CommandController;
import com.port.controller.model.ConsoleHandler;
import com.port.controller.model.Operator;
import com.port.port.PortController;
import com.port.port.model.StatisticObject;
import com.port.timetable.TimetableGenerator;
import com.port.timetable.model.Ship;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.function.UnaryOperator;

import static com.port.controller.model.ConsoleHandler.ErrorType.*;
import static com.port.controller.model.ConsoleHandler.MessageType.*;

public class Program {

    private static final LinkedList<Ship> timetable = new LinkedList<>();
    private final static Scanner scanner = new Scanner(System.in);
    private final static long time = System.currentTimeMillis();
    private static CommandController controller;
    private static StatisticObject lastStatistic = null;

    public static void main(String[] args) {
        Operator<String> handleUserInput = scanner::nextLine;
        controller = new CommandController(handleUserInput, getCommandsHandlers());
        controller.start();
    }

    private static Operator[] getCommandsHandlers() {
        final Operator<Void> generate = Program::generateNewTimetable;
        final Operator<Void> show = Program::showTimetable;
        final Operator<Void> add = Program::addNewShip;
        final Operator<Void> start = Program::startSimulation;
        final Operator<Void> stats = Program::showStatistic;
        final Operator<Void> quit = Program::quit;
        return new Operator[]{generate, show, add, start, stats, quit};
    }

    private static Void generateNewTimetable() {
        ConsoleHandler.printMessage(TIMETABLE_GENERATE_START);
        timetable.clear();
        timetable.addAll(TimetableGenerator.generate(time));
        String json = new Gson().toJson(timetable);
        try {
            FileWriter fileWriter = new FileWriter(PortController.TIMETABLE_FILE);
            fileWriter.write(json);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            ConsoleHandler.printError(FILE_ERROR);
        }
        ConsoleHandler.printMessage(TIMETABLE_GENERATE_END);
        return null;
    }

    private static Void showTimetable() {
        System.out.println("Вывод расписания:\n");
        if (timetable.isEmpty()) {
            ConsoleHandler.printError(NO_TIMETABLE);
        }
        timetable.forEach(System.out::println);
        return null;
    }

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
            timetable.add(ship);
            ConsoleHandler.printMessage(SHIP_ADDED);
            Collections.sort(timetable);
        } catch (Exception e) {
            ConsoleHandler.printError(BAD_INPUT);
        }

        return null;
    }

    private static Void startSimulation() {
        if (timetable.isEmpty()) {
            ConsoleHandler.printError(NO_TIMETABLE);
        } else {
            controller.end();
            final UnaryOperator<StatisticObject> onEnd = statisticObject -> {
                ConsoleHandler.printMessage(SIMULATION_END);
                lastStatistic = statisticObject;
                controller.start();
                return null;
            };
            ConsoleHandler.printMessage(SIMULATION_START);
            ConsoleHandler.setCyanColor();
            final PortController portController;
            try {
                portController = new PortController(time, onEnd);
                portController.initPort();
            } catch (FileNotFoundException e) {
                ConsoleHandler.printError(FILE_ERROR);
            }
        }
        return null;
    }

    private static Void showStatistic() {
        if (lastStatistic != null) {
            System.out.println(lastStatistic);
            if (ConsoleHandler.askUser(PRINT_SHIPS, scanner)) {
                lastStatistic.printShipStat();
            }
        } else {
            ConsoleHandler.printError(NO_SIMULATIONS);
        }
        return null;
    }

    private static Void quit() {
        if (ConsoleHandler.askUser(QUIT, scanner)) {
            controller.end();
        }
        return null;
    }

}
