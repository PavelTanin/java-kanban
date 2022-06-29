package ru.yandex.praktikum.tasktracker.services;

import ru.yandex.praktikum.tasktracker.data.EpicTask;
import ru.yandex.praktikum.tasktracker.data.Subtask;
import ru.yandex.praktikum.tasktracker.data.Task;

import java.io.IOException;
import java.util.List;

public interface TaskManager {

    void addSimpleTask(Task task);

    List<Task> getSimpleTaskList();

    void deleteAllSimpleTask();

    Task searchSimpleTaskById(int id);

    void updateSimpleTask(int id, Task task);

    void deleteSimpleTaskById(int id);

    void addEpicTask(EpicTask task);

    List<EpicTask> getEpicTaskList();

    void deleteAllEpicTask();

    EpicTask searchEpicTaskById(int id);

    void updateEpicTask(int id, EpicTask task);

    void deleteEpicTaskById(int id);

    List<Subtask> getSubTaskOfEpicList(int id);

    void updateSubTask(int id, Subtask task);

    Subtask searchSubTaskById(int id);

    void deleteAllSubTask();

    void addSubTask(Subtask task, int epicTaskId);

    List<Subtask> getSubTaskList();

    void deleteSubTaskById(int id);

    List<Task> getHistory() throws IOException;

}
