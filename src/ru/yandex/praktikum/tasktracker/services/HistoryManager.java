package ru.yandex.praktikum.tasktracker.services;

import ru.yandex.praktikum.tasktracker.data.Task;

import java.util.List;

public interface HistoryManager {

    void addTask(Task task);

    List<Task> getHistory();
}
