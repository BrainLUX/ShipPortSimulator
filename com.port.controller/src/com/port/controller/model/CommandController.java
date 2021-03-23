package com.port.controller.model;

import static com.port.controller.model.ConsoleHandler.MessageType.CHOOSE_ACTION;

public class CommandController {
    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_BLACK = "\u001B[30m";
    static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_GREEN = "\u001B[32m";
    static final String ANSI_YELLOW = "\u001B[33m";
    static final String ANSI_BLUE = "\u001B[34m";
    static final String ANSI_PURPLE = "\u001B[35m";
    static final String ANSI_CYAN = "\u001B[36m";
    static final String ANSI_WHITE = "\u001B[37m";

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
        ConsoleHandler.printMessage(CHOOSE_ACTION);
        for (Command command : COMMANDS) {
            System.out.println((command.ordinal() + 1) + ". " + command.getTitle());
        }
    }

    public enum Command {
        GENERATE(ANSI_BLUE + "Сгенерировать новое расписание" + ANSI_RESET),
        SHOW("Посмотреть текущее расписание"),
        ADD("Добавить новый корабль"),
        START(ANSI_CYAN + "Начать симуляцию" + ANSI_RESET),
        STATS("Посмотреть статистику по последней симуляции"),
        QUIT(ANSI_RED + "Выход из программы" + ANSI_RESET);

        private final String title;

        Command(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }
}
