package ru.yandex.praktikum.tasktracker.services;

import ru.yandex.praktikum.tasktracker.data.EpicTask;
import ru.yandex.praktikum.tasktracker.data.Status;
import ru.yandex.praktikum.tasktracker.data.Subtask;
import ru.yandex.praktikum.tasktracker.data.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class TaskManager {
    private Integer id = 0;
    private ArrayList<Integer> oldIds = new ArrayList<>();

    final HashMap<Integer, Task> simpleTasks = new HashMap<>();

    final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    final HashMap<Integer, EpicTask> epics = new HashMap<>();

    public void addSimpleTask(Task task) {
        id = idGenerator();
        task.setId(id);
        task.setStatus(String.valueOf(Status.NEW));
        simpleTasks.put(id, task);
    }

    public String getSimpleTaskList() {
        return String.valueOf(simpleTasks);
    }

    public void deleteAllSimpleTask() {
        for (Task item : simpleTasks.values()) {
            oldIds.add(item.getId());
        }
        sortDeletedId();
        simpleTasks.clear();
    }

    public String searchSimpleTaskById(int id) {
        if (simpleTasks.get(id) == null) {
            return "Задачи с таким номером нет";
        }
        return String.valueOf(simpleTasks.get(id).toString());
    }

    public void updateSimpleTask(int id, String newDiscription, String status) {
        simpleTasks.get(id).setDiscription(newDiscription);
        if (status.equals("Новая")) {
            subtasks.get(id).setStatus(String.valueOf(Status.NEW));
        } else if (status.equals("В процессе")) {
            subtasks.get(id).setStatus(String.valueOf(Status.IN_PROGRESS));
        } else if (status.equals("Выполнено")) {
            subtasks.get(id).setStatus(String.valueOf(Status.DONE));
        }
    }

    public void deleteSimpleTaskById(int id) {
        oldIds.add(simpleTasks.get(id).getId());
        sortDeletedId();
        simpleTasks.remove(id);
    }

    public void addEpicTask(EpicTask task) {
        id++;
        task.setId(id);
        task.setStatus(epicTaskStatus(task));
        epics.put(id, task);
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
        return String.valueOf(epics);
    }

    public void deleteAllEpicTask() {
        for (EpicTask item : epics.values()) {
            oldIds.add(item.getId());
        }
        for (Subtask item : subtasks.values()) {
            oldIds.add(item.getId());
        }
        sortDeletedId();
        epics.clear();
        subtasks.clear();
    }

    public String searchEpicTaskById(int id) {
        if (epics.get(id) == null) {
            return "Задачи с таким номером нет";
        }
        return String.valueOf(epics.get(id).toString());
    }

    public void updateEpicTask(int id, EpicTask task) {
        epics.get(id).setName(task.getName());
        epics.get(id).setDiscription(task.getDiscription());
    }

    public void deleteEpicTaskById(int id) {
        ArrayList<Subtask> subtaskList = epics.get(id).getSubtasks();
        for (Subtask item : subtaskList) {
            oldIds.add(item.getId());
            subtasks.remove(item.getId());
        }
        for (EpicTask item : epics.values()) {
            oldIds.add(item.getId());
        }
        sortDeletedId();
        epics.remove(id);
    }

    public String getSubTaskOfEpicList(int id) {
        ArrayList<Subtask> subtasksOfEpic = epics.get(id).getSubtasks();
        return "Подзадачи, входящие в эту задачу :\n" + String.valueOf(subtasksOfEpic);
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

    public String searchSubTaskById(int id) {
        if (subtasks.get(id) == null) {
            return "Задачи с таким номером нет";
        }
        return String.valueOf(subtasks.get(id).toString());
    }

    public void deleteAllSubTask() {
        for (Task item : subtasks.values()) {
            oldIds.add(item.getId());
        }
        sortDeletedId();
        subtasks.clear();
    }

    public void addSubTask(Subtask task, int epicTaskId) {
        id++;
        task.setId(id);
        task.setStatus(String.valueOf(Status.NEW));
        task.setEpicName(epics.get(epicTaskId).getName());
        subtasks.put(id, task);
        epics.get(epicTaskId).addSubtask(task);
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
        oldIds.add(subtasks.get(id).getId());
        sortDeletedId();
        subtasks.remove(id);
    }

    private int idGenerator() {
        if (!(oldIds.isEmpty())) {
            id++;
        } else {
            id = oldIds.get(0);
            oldIds.remove(0);
        }
        return id;
    }

    private void sortDeletedId() {
        Collections.sort(oldIds);
    }


}
