package ru.yandex.praktikum.tasktracker.services;

import ru.yandex.praktikum.tasktracker.data.EpicTask;
import ru.yandex.praktikum.tasktracker.data.Status;
import ru.yandex.praktikum.tasktracker.data.Subtask;
import ru.yandex.praktikum.tasktracker.data.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    void addSimpleTask(Task task);

    ArrayList<Task> getSimpleTaskList();

    void deleteAllSimpleTask();

    Task searchSimpleTaskById(int id);

    void updateSimpleTask(int id, Task task);

    void deleteSimpleTaskById(int id);

    void addEpicTask(EpicTask task);

    void epicTaskStatus(EpicTask task);

    ArrayList<EpicTask> getEpicTaskList();

    void deleteAllEpicTask();

    EpicTask searchEpicTaskById(int id);

    void updateEpicTask(int id, EpicTask task);

    void deleteEpicTaskById(int id);

    ArrayList<Subtask> getSubTaskOfEpicList(int id);

    void updateSubTask(int id, Subtask task);

    Subtask searchSubTaskById(int id);

    void deleteAllSubTask();

    void addSubTask(Subtask task, int epicTaskId);

    ArrayList<Subtask> getSubTaskList();

    void deleteSubTaskById(int id);

    List<Task> getHistory();

}
