package ru.yandex.praktikum.tasktracker.interfaces;

import ru.yandex.praktikum.tasktracker.data.Task;

import java.util.List;

public interface HistoryManager {

    void linkLast(Task task);

    void remove(int id);

    List<Task> getHistory();

}
