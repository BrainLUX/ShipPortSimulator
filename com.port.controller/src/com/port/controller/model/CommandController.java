package com.port.controller.model;

public class CommandController {
    private final Command[] COMMANDS = Command.values();

    private final Operator<String> handleUserInput;
    private final Operator[] userCommands;

    private boolean isRunning = false;

    public CommandController(Operator<String> handleUserInput, Operator[] userCommands) {
        this.handleUserInput = handleUserInput;
        this.userCommands = userCommands;
    }

    public void start() {
        isRunning = true;
        while (isRunning) {
            printCommands();
            checkUserInput();
        }
    }

    public void end() {
        isRunning = false;
    }

    private void checkUserInput() {
        String input = handleUserInput.execute();
        int code = Integer.parseInt(input) - 1;
        userCommands[code].execute();
    }

    private void printCommands() {
        System.out.println("Выберите действие:\n");
        for (Command command : COMMANDS) {
            System.out.println((command.ordinal() + 1) + ". " + command.getTitle());
        }
    }

    public enum Command {
        GENERATE("Сгенерировать новое расписание"),
        SHOW("Посмотреть текущее расписание"),
        ADD("Добавить новый корабль"),
        START("Начать симуляцию"),
        STATS("Посмотреть статистику по последней симуляции"),
        QUIT("Выход из программы");

        private final String title;

        Command(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }
}
