package ru.yandex.praktikum.tasktracker.services;

public class Manager<T extends TaskManager> {

    private static final TaskManager taskManager = new InMemoryTaskManager();

    private static final HistoryManager historyManager = new InMemoryHistoryManager();

    public static TaskManager getDefault() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistory(){
        return historyManager;
    }
}