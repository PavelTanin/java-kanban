package ru.yandex.praktikum.tasktracker.services;

import ru.yandex.praktikum.tasktracker.data.EpicTask;
import ru.yandex.praktikum.tasktracker.data.Status;
import ru.yandex.praktikum.tasktracker.data.Subtask;
import ru.yandex.praktikum.tasktracker.data.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TaskManager {
    int id = 0;

    Scanner scanner = new Scanner(System.in);

    private HashMap<Integer, Task> simpleTasks = new HashMap<>();

    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private HashMap<Integer, EpicTask> epics = new HashMap<>();

    public String addSimpleTask(Task task) {
        id++;
        task.setId(id);
        task.setStatus(String.valueOf(Status.NEW));
        simpleTasks.put(id, task);
        return String.valueOf(task.toString());
    }

    public String getSimpleTaskList() {
        return String.valueOf(simpleTasks);
    }

    public void deleteAllSimpleTask() {
        simpleTasks.clear();
    }

    public String searchSimpleTaskById(int id) {
        int errorCount = 0;
        String result = null;
        for (Integer item : simpleTasks.keySet()) {
            if (item.equals(id)) {
                result = String.valueOf(simpleTasks.get(id).toString());
            } else {
                errorCount++;
            }
        }
        if (errorCount > 0) {
            return "Задачи с таким номером нет";
        }
        return result;
    }

    public void updateSimpleTask(int id, String newDiscription, int status) {
        simpleTasks.get(id).setDiscription(newDiscription);
        if (status == 1) {
            simpleTasks.get(id).setStatus(String.valueOf(Status.NEW));
        } else if (status == 2) {
            simpleTasks.get(id).setStatus(String.valueOf(Status.IN_PROGRESS));
        } else if (status == 3) {
            simpleTasks.get(id).setStatus(String.valueOf(Status.DONE));
        }
    }

    public void deleteSimpleTaskById(int id) {
        simpleTasks.remove(id);
    }

    public String addEpicTask(EpicTask task) {
        id++;
        task.setId(id);
        task.setStatus(epicTaskStatus(task));
        epics.put(id, task);
        return task.toString();
    }

    private String epicTaskStatus(EpicTask task) {
        int newStatusCount = 0;
        int doneStatusCount = 0;
        ArrayList<Subtask> subtasksName = task.getSubtasks();
        if (subtasksName.isEmpty()) {
            return String.valueOf(Status.NEW);
        } else {
            for (Subtask listCount : subtasksName) {
                if (listCount.getStatus().equals(String.valueOf(Status.NEW))) {
                    newStatusCount++;
                } else if (listCount.getStatus().equals(String.valueOf(Status.DONE))) {
                    doneStatusCount++;
                }
            }
            if (newStatusCount == subtasksName.size()) {
                return String.valueOf(Status.NEW);
            } else if (doneStatusCount == subtasksName.size()) {
                return String.valueOf(Status.DONE);
            } else {
                return String.valueOf(Status.IN_PROGRESS);
            }
        }
    }

    public String getEpicTaskList() {
        for (EpicTask item : epics.values()) {
            item.setStatus(epicTaskStatus(item));
        }
        return String.valueOf(epics);
    }

    public void deleteAllEpicTask() {
        epics.clear();
        subtasks.clear();
    }

    public String searchEpicTaskById(int id) {
        int errorCount = 0;
        String result = null;
        for (Integer item : epics.keySet()) {
            if (item.equals(id)) {
                epics.get(item).setStatus(epicTaskStatus(epics.get(item)));
                result = String.valueOf(epics.get(id).toString());
            } else {
                errorCount++;
            }
        }
        if (errorCount > 0) {
            return "Задачи с таким номером нет";
        }
        return result;
    }

    public void updateEpicTask(int id, String newDiscription) {
        epics.get(id).setDiscription(newDiscription);
    }

    public void deleteEpicTaskById(int id) {
        ArrayList<Subtask> subtaskList = epics.get(id).getSubtasks();
        for (Subtask item : subtaskList) {
            subtasks.remove(item.getId());
        }
        epics.remove(id);
    }

    public void updateSubTask(int id, String newDiscription, String status) {
        subtasks.get(id).setDiscription(newDiscription);
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

    public String searchSubTaskById(int id) {
        int errorCount = 0;
        String result = null;
        for (Integer item : subtasks.keySet()) {
            if (item.equals(id)) {
                result = String.valueOf(subtasks.get(id).toString());
            } else {
                errorCount++;
            }
        }
        if (errorCount > 0) {
            return "Подзадачи с таким номером нет";
        }
        return result;
    }


    public void deleteAllSubTask() {
        subtasks.clear();
    }

    public String addSubTask(Subtask task, int epicTaskId) {
        id++;
        task.setId(id);
        task.setStatus(String.valueOf(Status.NEW));
        task.setEpicName(epics.get(epicTaskId).getName());
        subtasks.put(id, task);
        epics.get(epicTaskId).addSubtasks(task);
        return task.toString();
    }


    public String getSubTaskList() {
        return String.valueOf(subtasks);
    }

    public void deleteSubTaskById(int id) {
        for (EpicTask item : epics.values()) {
            if (item.getName().equals(subtasks.get(id).getEpicName())) {
                item.removeSubTask(subtasks.get(id));
                epicTaskStatus(item);
            }
        }
        subtasks.remove(id);
    }


}
