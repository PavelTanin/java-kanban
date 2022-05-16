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

    private final InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

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
    public ArrayList<Task> getSimpleTaskList() {
        ArrayList<Task> result = new ArrayList<>();
        for (Task item : simpleTasks.values()) {
            result.add(item);
        }
        return result;
    }

    @Override
    public void deleteAllSimpleTask() {
        simpleTasks.clear();
    }

    @Override
    public Task searchSimpleTaskById(int id) {
        if (simpleTasks.get(id) == null) {
            return null;
        }
        inMemoryHistoryManager.addTask(simpleTasks.get(id));
        return simpleTasks.get(id);
    }

    @Override
    public void updateSimpleTask(int id, Task task) {
        simpleTasks.get(id).setName(task.getName());
        simpleTasks.get(id).setDiscription(task.getDiscription());
        switch (task.getStatus()) {
            case NEW:
                subtasks.get(id).setStatus(Status.NEW);
                break;
            case IN_PROGRESS:
                subtasks.get(id).setStatus(Status.IN_PROGRESS);
                break;
            case DONE:
                subtasks.get(id).setStatus(Status.DONE);
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

    @Override
    public void epicTaskStatus(EpicTask task) {
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
    public ArrayList<EpicTask> getEpicTaskList() {
        ArrayList<EpicTask> result = new ArrayList<>();
        for (EpicTask item : epics.values()) {
            epicTaskStatus(item);
            result.add(item);
        }
        return result;
    }

    @Override
    public void deleteAllEpicTask() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public EpicTask searchEpicTaskById(int id) {
        if (epics.get(id) == null) {
            return null;
        }
        inMemoryHistoryManager.addTask(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void updateEpicTask(int id, EpicTask task) {
        epics.get(id).setName(task.getName());
        epics.get(id).setDiscription(task.getDiscription());
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
    public ArrayList<Subtask> getSubTaskOfEpicList(int id) {
        ArrayList<Subtask> result = new ArrayList<>();
        ArrayList<Integer> subIds = epics.get(id).getSubtasks();
        for (Integer subId : subIds) {
            result.add(subtasks.get(subId));
        }
        return result;
    }

    @Override
    public void updateSubTask(int id, Subtask task) {
        subtasks.get(id).setDiscription(task.getDiscription());
        subtasks.get(id).setName(task.getName());
        switch (task.getStatus()) {
            case NEW:
                subtasks.get(id).setStatus(Status.NEW);
                break;
            case IN_PROGRESS:
                subtasks.get(id).setStatus(Status.IN_PROGRESS);
                break;
            case DONE:
                subtasks.get(id).setStatus(Status.DONE);
                break;
        }
        for (EpicTask item : epics.values()) {
            epicTaskStatus(item);
        }
    }

    @Override
    public Subtask searchSubTaskById(int id) {
        if (subtasks.get(id) == null) {
            return null;
        }
        inMemoryHistoryManager.addTask(subtasks.get(id));
        return subtasks.get(id);
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
    }

    @Override
    public ArrayList<Subtask> getSubTaskList() {
        ArrayList<Subtask> result = new ArrayList<>();
        for (Subtask item : subtasks.values()) {
            result.add(item);
        }
        return result;
    }

    @Override
    public void deleteSubTaskById(int id) {
        epics.get(subtasks.get(id).getEpicId()).removeSubTask(subtasks.get(id).getId());
        subtasks.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }


    private int idGenerator() {
        id++;
        return id;
    }


}
