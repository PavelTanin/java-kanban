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
    public ArrayList<Task> getSimpleTaskList() {
        return new ArrayList<>(simpleTasks.values());
    }

    @Override
    public void deleteAllSimpleTask() {
        if (!simpleTasks.isEmpty()) {
            for (Integer id : simpleTasks.keySet()) {
                Manager.getDefaultHistory().remove(id);
            }
            simpleTasks.clear();
        }
    }

    @Override
    public Task searchSimpleTaskById(int id) {
        if (simpleTasks.get(id) == null) {
            return null;
        }
        Manager.getDefaultHistory().linkLast(simpleTasks.get(id));
        return simpleTasks.get(id);
    }

    @Override
    public void updateSimpleTask(int id, Task task) {
        if (simpleTasks.containsKey(id) && task != null) {
            var simpleTask = simpleTasks.get(id);
            simpleTask.setName(task.getName());
            simpleTask.setDiscription(task.getDiscription());
            switch (task.getStatus()) {
                case NEW:
                    simpleTask.setStatus(Status.NEW);
                    break;
                case IN_PROGRESS:
                    simpleTask.setStatus(Status.IN_PROGRESS);
                    break;
                case DONE:
                    simpleTask.setStatus(Status.DONE);
                    break;
            }
        }
    }

    @Override
    public void deleteSimpleTaskById(int id) {
        if (simpleTasks.containsKey(id)) {
            Manager.getDefaultHistory().remove(id);
            simpleTasks.remove(id);
        }
    }

    @Override
    public void addEpicTask(EpicTask task) {
        id = idGenerator();
        task.setId(id);
        epicTaskStatus(task);
        epics.put(id, task);
    }


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
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpicTask() {
        if (!epics.isEmpty()) {
            for (Integer epicId : epics.keySet()) {
                ArrayList<Integer> subtaskIds = epics.get(epicId).getSubtasks();
                for (Integer subId : subtaskIds) {
                    Manager.getDefaultHistory().remove(subId);
                }
                Manager.getDefaultHistory().remove(epicId);
            }
            epics.clear();
            subtasks.clear();
        }
    }

    @Override
    public EpicTask searchEpicTaskById(int id) {
        if (epics.get(id) == null) {
            return null;
        }
        Manager.getDefaultHistory().linkLast(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void updateEpicTask(int id, EpicTask task) {
        if (epics.containsKey(id) && task != null) {
            epics.get(id).setName(task.getName());
            epics.get(id).setDiscription(task.getDiscription());
        }
    }

    @Override
    public void deleteEpicTaskById(int id) {
        if (epics.containsKey(id)) {
            Manager.getDefaultHistory().remove(id);
            ArrayList<Integer> subtasksId = epics.get(id).getSubtasks();
            for (Integer subtaskId : subtasksId) {
                Manager.getDefaultHistory().remove(subtaskId);
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        }
    }

    @Override
    public ArrayList<Subtask> getSubTaskOfEpicList(int id) {
        ArrayList<Subtask> result = new ArrayList<>();
        if (epics.containsKey(id)) {
            ArrayList<Integer> subIds = epics.get(id).getSubtasks();
            for (Integer subId : subIds) {
                result.add(subtasks.get(subId));
            }
        }
        return result;
    }

    @Override
    public void updateSubTask(int id, Subtask task) {
        if (subtasks.containsKey(id) && task != null) {
            var subtask = subtasks.get(id);
            subtask.setDiscription(task.getDiscription());
            subtask.setName(task.getName());
            switch (task.getStatus()) {
                case NEW:
                    subtask.setStatus(Status.NEW);
                    break;
                case IN_PROGRESS:
                    subtask.setStatus(Status.IN_PROGRESS);
                    break;
                case DONE:
                    subtask.setStatus(Status.DONE);
                    break;
            }
            epicTaskStatus(epics.get(subtask.getEpicId()));
        }
    }

    @Override
    public Subtask searchSubTaskById(int id) {
        if (subtasks.get(id) == null) {
            return null;
        }
        Manager.getDefaultHistory().linkLast(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void deleteAllSubTask() {
        if (!subtasks.isEmpty()) {
            for (EpicTask item : epics.values()) {
                item.removeAllSubTasks();
            }
            for (Integer id : subtasks.keySet()) {
                Manager.getDefaultHistory().remove(id);
            }
            subtasks.clear();
        }
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
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteSubTaskById(int id) {
        if (subtasks.containsKey(id)) {
            Manager.getDefaultHistory().remove(id);
            epics.get(subtasks.get(id).getEpicId()).removeSubTask(subtasks.get(id).getId());
            subtasks.remove(id);
        }
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
