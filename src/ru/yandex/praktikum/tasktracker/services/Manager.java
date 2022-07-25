package ru.yandex.praktikum.tasktracker.services;

import ru.yandex.praktikum.tasktracker.interfaces.HistoryManager;
import ru.yandex.praktikum.tasktracker.interfaces.TaskManager;

public class Manager<T extends TaskManager> {

    private static TaskManager taskManager = taskManager = new HttpTaskManager("http://localhost:8078", "Test");

    private static final HistoryManager historyManager = new InMemoryHistoryManager();

    public static TaskManager getDefault() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }
}
