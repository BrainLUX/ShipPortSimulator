package com.port.controller.model;

public class ConsoleHandler {
    public static void printError(ErrorType type) {
        print(CommandController.ANSI_RED, type.getMessage());
    }

    public static void printMessage(MessageType message) {
        print(CommandController.ANSI_GREEN, message.getMessage());
    }

    private static void print(String color, String message) {
        setColor(color);
        System.out.println(message);
        System.out.println();
        resetColor();
    }

    public static void setCyanColor() {
        setColor(CommandController.ANSI_CYAN);
    }

    private static void resetColor() {
        setColor(CommandController.ANSI_RESET);
    }

    private static void setColor(String color) {
        System.out.print(color);
    }

    public enum ErrorType {
        NO_TIMETABLE("Вы ещё не сгенерировали ни одного расписания"),
        NO_SIMULATIONS("Сначала нужно провести симуляцию"),
        BAD_INPUT("Проверьте корректность введённых данных");

        private final String message;

        ErrorType(String title) {
            this.message = title;
        }

        public String getMessage() {
            return message;
        }
    }

    public enum MessageType {
        SIMULATION_START("Начинаю симуляцию"),
        SIMULATION_END("Симуляция успешно завершена"),
        CHOOSE_ACTION("Выберите действие:"),
        SHIP_ADDED("Корабль успешно добавлен"),
        TIMETABLE_GENERATE_START("Генерирую расписание"),
        TIMETABLE_GENERATE_END("Расписание сгенерировано успешно");

        private final String message;

        MessageType(String title) {
            this.message = title;
        }

        public String getMessage() {
            return message;
        }
    }
}
