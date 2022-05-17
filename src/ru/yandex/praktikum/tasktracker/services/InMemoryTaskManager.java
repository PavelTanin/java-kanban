package ru.yandex.praktikum.tasktracker.services;

import ru.yandex.praktikum.tasktracker.data.EpicTask;
import ru.yandex.praktikum.tasktracker.data.Status;
import ru.yandex.praktikum.tasktracker.data.Subtask;
import ru.yandex.praktikum.tasktracker.data.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private Integer id = 0;

    private final HashMap<Integer, Task> simpleTasks = new HashMap<>();

    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private final HashMap<Integer, EpicTask> epics = new HashMap<>();

    @Override
    public void addSimpleTask(Task task) {
        id = idGenerator();
        task.setId(id);
        task.setStatus(Status.NEW);
        simpleTasks.put(id, task);
    }

    @Override
    public List<Task> getSimpleTaskList() {
        return new ArrayList<>(simpleTasks.values());
    }

    @Override
    public void deleteAllSimpleTask() {
        simpleTasks.clear();
    }

    @Override
    public Task searchSimpleTaskById(int id) {
        var task = simpleTasks.get(id);
        if (task == null) {
            return null;
        }
        Manager.getDefaultHistory().addTask(task);
        return task;
    }

    @Override
    public void updateSimpleTask(int id, Task task) {
        var updatedTask = simpleTasks.get(id);
        updatedTask.setName(task.getName());
        updatedTask.setDiscription(task.getDiscription());
        switch (updatedTask.getStatus()) {
            case NEW:
                updatedTask.setStatus(Status.NEW);
                break;
            case IN_PROGRESS:
                updatedTask.setStatus(Status.IN_PROGRESS);
                break;
            case DONE:
                updatedTask.setStatus(Status.DONE);
                break;
        }
    }

    @Override
    public void deleteSimpleTaskById(int id) {
        simpleTasks.remove(id);
    }

    @Override
    public void addEpicTask(EpicTask task) {
        id = idGenerator();
        task.setId(id);
        epicTaskStatus(task);
        epics.put(id, task);
    }

    private void epicTaskStatus(EpicTask task) {
        int newStatusCount = 0;
        int doneStatusCount = 0;
        ArrayList<Integer> subtasksId = task.getSubtasks();
        if (subtasksId.isEmpty()) {
            task.setStatus(Status.NEW);
        }
        for (Integer id : subtasksId) {
            if (subtasks.get(id).getStatus().equals(Status.NEW)) {
                newStatusCount++;
            } else if (subtasks.get(id).getStatus().equals(Status.DONE)) {
                doneStatusCount++;
            }
        }
        if (newStatusCount == subtasksId.size()) {
            task.setStatus(Status.NEW);
        } else if (doneStatusCount == subtasksId.size()) {
            task.setStatus(Status.DONE);
        } else {
            task.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public List<EpicTask> getEpicTaskList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpicTask() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public EpicTask searchEpicTaskById(int id) {
        var task = epics.get(id);
        if (task == null) {
            return null;
        }
        Manager.getDefaultHistory().addTask(task);
        return task;
    }

    @Override
    public void updateEpicTask(int id, EpicTask task) {
        var updatedTask = epics.get(id);
        updatedTask.setName(task.getName());
        updatedTask.setDiscription(task.getDiscription());
    }

    @Override
    public void deleteEpicTaskById(int id) {
        ArrayList<Integer> subtasksId = epics.get(id).getSubtasks();
        for (Integer subtaskId : subtasksId) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    @Override
    public List<Subtask> getSubTaskOfEpicList(int id) {
        ArrayList<Subtask> result = new ArrayList<>();
        ArrayList<Integer> subIds = epics.get(id).getSubtasks();
        for (Integer subId : subIds) {
            result.add(subtasks.get(subId));
        }
        return result;
    }

    @Override
    public void updateSubTask(int id, Subtask task) {
        var updatedTask = subtasks.get(id);
        updatedTask.setDiscription(task.getDiscription());
        updatedTask.setName(task.getName());
        switch (task.getStatus()) {
            case NEW:
                updatedTask.setStatus(Status.NEW);
                break;
            case IN_PROGRESS:
                updatedTask.setStatus(Status.IN_PROGRESS);
                break;
            case DONE:
                updatedTask.setStatus(Status.DONE);
                break;
        }
        epicTaskStatus(epics.get(subtasks.get(id).getEpicId()));
    }

    @Override
    public Subtask searchSubTaskById(int id) {
        var task = subtasks.get(id);
        if (task == null) {
            return null;
        }
        Manager.getDefaultHistory().addTask(task);
        return task;
    }

    @Override
    public void deleteAllSubTask() {
        for (EpicTask item : epics.values()) {
            item.removeAllSubTasks();
        }
        subtasks.clear();
    }

    @Override
    public void addSubTask(Subtask task, int epicTaskId) {
        id = idGenerator();
        task.setId(id);
        task.setStatus(Status.NEW);
        task.setEpicName(epics.get(epicTaskId).getName());
        subtasks.put(id, task);
        epics.get(epicTaskId).addSubtask(id);
        epicTaskStatus(epics.get(epicTaskId));
    }

    @Override
    public List<Subtask> getSubTaskList() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteSubTaskById(int id) {
        epics.get(subtasks.get(id).getEpicId()).removeSubTask(subtasks.get(id).getId());
        epicTaskStatus(epics.get(subtasks.get(id).getEpicId()));
        subtasks.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return Manager.getDefaultHistory().getHistory();
    }

    private int idGenerator() {
        id++;
        return id;
    }


}
