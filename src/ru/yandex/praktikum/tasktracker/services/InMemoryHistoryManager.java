package ru.yandex.praktikum.tasktracker.services;

import ru.yandex.praktikum.tasktracker.data.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private final List<Task> historyList = new LinkedList<>();

    @Override
    public void addTask(Task task) {
        if (historyList.size() == 10) {
            historyList.remove(0);
        }
        historyList.add(task);

    }
    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
