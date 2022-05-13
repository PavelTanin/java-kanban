package ru.yandex.praktikum.tasktracker.services;

import ru.yandex.praktikum.tasktracker.data.EpicTask;
import ru.yandex.praktikum.tasktracker.data.Status;
import ru.yandex.praktikum.tasktracker.data.Subtask;
import ru.yandex.praktikum.tasktracker.data.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private Integer id = 0;

    private final HashMap<Integer, Task> simpleTasks = new HashMap<>();

    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private final HashMap<Integer, EpicTask> epics = new HashMap<>();

    public void addSimpleTask(Task task) {
        id = idGenerator();
        task.setId(id);
        task.setStatus(String.valueOf(Status.NEW));
        simpleTasks.put(id, task);
    }

    public ArrayList<Task> getSimpleTaskList() {
        ArrayList<Task> result = new ArrayList<>();
        for (Task item : simpleTasks.values()) {
            result.add(item);
        }
        return result;
    }

    public void deleteAllSimpleTask() {
        simpleTasks.clear();
    }

    public Task searchSimpleTaskById(int id) {
        return simpleTasks.get(id);
    }

    public void updateSimpleTask(int id, Task task, String status) {
        simpleTasks.get(id).setName(task.getName());
        simpleTasks.get(id).setDiscription(task.getDiscription());
        if (status.equals("Новая")) {
            subtasks.get(id).setStatus(String.valueOf(Status.NEW));
        } else if (status.equals("В процессе")) {
            subtasks.get(id).setStatus(String.valueOf(Status.IN_PROGRESS));
        } else if (status.equals("Выполнено")) {
            subtasks.get(id).setStatus(String.valueOf(Status.DONE));
        }
    }

    public void deleteSimpleTaskById(int id) {
        simpleTasks.remove(id);
    }

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
            task.setStatus(String.valueOf(Status.NEW));
        }
        for (Integer id : subtasksId) {
            if (subtasks.get(id).getStatus().equals(String.valueOf(Status.NEW))) {
                newStatusCount++;
            } else if (subtasks.get(id).getStatus().equals(String.valueOf(Status.DONE))) {
                doneStatusCount++;
            }
        }
        if (newStatusCount == subtasksId.size()) {
            task.setStatus(String.valueOf(Status.NEW));
        } else if (doneStatusCount == subtasksId.size()) {
            task.setStatus(String.valueOf(Status.DONE));
        } else {
            task.setStatus(String.valueOf(Status.IN_PROGRESS));;
        }
    }


    public ArrayList<EpicTask> getEpicTaskList() {
        ArrayList<EpicTask> result = new ArrayList<>();
        for (EpicTask item : epics.values()) {
            epicTaskStatus(item);
            result.add(item);
        }
        return result;
    }

    public void deleteAllEpicTask() {
        epics.clear();
        subtasks.clear();
    }

    public EpicTask searchEpicTaskById(int id) {
        if (epics.get(id) == null) {
            return null;
        }
        return epics.get(id);
    }

    public void updateEpicTask(int id, EpicTask task) {
        epics.get(id).setName(task.getName());
        epics.get(id).setDiscription(task.getDiscription());
    }

    public void deleteEpicTaskById(int id) {
        ArrayList<Integer> subtasksId = epics.get(id).getSubtasks();
        for (Integer subtaskId : subtasksId) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    public ArrayList<Integer> getSubTaskOfEpicList(int id) {
        ArrayList<Integer> result = epics.get(id).getSubtasks();
        return result;
    }

    public void updateSubTask(int id, Subtask task, String status) {
        subtasks.get(id).setDiscription(task.getDiscription());
        subtasks.get(id).setName(task.getName());
        if (status.equals("Новая")) {
            subtasks.get(id).setStatus(String.valueOf(Status.NEW));
        } else if (status.equals("В процессе")) {
            subtasks.get(id).setStatus(String.valueOf(Status.IN_PROGRESS));
        } else if (status.equals("Выполнено")) {
            subtasks.get(id).setStatus(String.valueOf(Status.DONE));
        }
        for (EpicTask item : epics.values()) {
            epicTaskStatus(item);
        }
    }

    public Subtask searchSubTaskById(int id) {
        if (subtasks.get(id) == null) {
            return null;
        }
        return subtasks.get(id);
    }

    public void deleteAllSubTask() {
        for (EpicTask item : epics.values()) {
            item.removeAllSubTasks();
        }
        subtasks.clear();
    }

    public void addSubTask(Subtask task, int epicTaskId) {
        id = idGenerator();
        task.setId(id);
        task.setStatus(String.valueOf(Status.NEW));
        task.setEpicName(epics.get(epicTaskId).getName());
        subtasks.put(id, task);
        epics.get(epicTaskId).addSubtask(id);
    }

    public ArrayList<Subtask> getSubTaskList() {
        ArrayList<Subtask> result = new ArrayList<>();
        for (Subtask item : subtasks.values()) {
            result.add(item);
        }
        return result;
    }

    public void deleteSubTaskById(int id) {
        epics.get(subtasks.get(id).getEpicId()).removeSubTask(subtasks.get(id).getId());
        subtasks.remove(id);
    }

    private int idGenerator() {
        id++;
        return id;
    }


}
