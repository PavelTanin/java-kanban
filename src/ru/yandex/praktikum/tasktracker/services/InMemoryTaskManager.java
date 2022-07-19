package ru.yandex.praktikum.tasktracker.services;

import ru.yandex.praktikum.tasktracker.data.EpicTask;
import ru.yandex.praktikum.tasktracker.data.Status;
import ru.yandex.praktikum.tasktracker.data.Subtask;
import ru.yandex.praktikum.tasktracker.data.Task;
import ru.yandex.praktikum.tasktracker.interfaces.TaskManager;

import java.time.Duration;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected Integer id = 0;

    protected final Comparator<Task> timeComparator = (Task o1, Task o2) -> {
        if (o1 != null && o2 != null) {
            if (o1.getStartTime() == null) {
                return 1;
            } else if (o2.getStartTime() == null) {
                return -1;
            } else if (o1.getStartTime().isAfter(o2.getStartTime())) {
                return 1;
            } else if (o1.getStartTime().isBefore(o2.getStartTime())) {
                return -1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    };

    protected final Set<Task> prioritizedTasks = new TreeSet<>(timeComparator);

    protected final HashMap<Integer, Task> simpleTasks = new HashMap<>();

    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    protected final HashMap<Integer, EpicTask> epics = new HashMap<>();

    protected final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    @Override
    public void addSimpleTask(Task task) {
        if (!isTaskCrossOther(task)) {
            id = idGenerator();
            task.setId(id);
            task.setStatus(Status.NEW);
            simpleTasks.put(id, task);
            prioritizedTasks.add(task);
        } else {
            throw new RuntimeException("Время выполнения задачи пересекается с другими!");
        }

    }

    @Override
    public ArrayList<Task> getSimpleTaskList() {
        return new ArrayList<>(simpleTasks.values());
    }

    @Override
    public void deleteAllSimpleTask() {
        if (!simpleTasks.isEmpty()) {
            for (Integer id : simpleTasks.keySet()) {
                historyManager.remove(id);
                prioritizedTasks.remove(simpleTasks.get(id));
            }
            simpleTasks.clear();
        } else {
            throw new RuntimeException("В списке нет простых задач!");
        }
    }

    @Override
    public Task searchSimpleTaskById(int id) {
        if (simpleTasks.get(id) == null) {
            return null;
        }
        historyManager.linkLast(simpleTasks.get(id));
        return simpleTasks.get(id);
    }

    @Override
    public void updateSimpleTask(int id, Task task) {
        if (simpleTasks.containsKey(id) && task != null) {
            var simpleTask = simpleTasks.get(id);
            prioritizedTasks.remove(simpleTask);
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
            prioritizedTasks.add(simpleTask);
        } else {
            throw new RuntimeException("Нет задачи с таким ID!");
        }
    }

    @Override
    public void deleteSimpleTaskById(int id) {
        if (simpleTasks.containsKey(id)) {
            historyManager.remove(id);
            simpleTasks.remove(id);
            prioritizedTasks.remove(simpleTasks.get(id));
        } else {
            throw new RuntimeException("Нет задачи с таким ID!");
        }
    }

    @Override
    public void addEpicTask(EpicTask task) {
        id = idGenerator();
        task.setId(id);
        epicTaskStatus(task);
        epics.put(id, task);
    }


    protected void epicTaskStatus(EpicTask task) {
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

    protected void epicTaskSetTime(EpicTask task) {
        Comparator<Subtask> comparator = (o1, o2) -> {
            if (o1.getStartTime().isAfter(o2.getStartTime())) {
                return 1;
            } else if (o1.getStartTime().isBefore(o2.getStartTime())) {
                return -1;
            } else {
                return 0;
            }
        };
        List<Integer> subtasksIds = task.getSubtasks();
        List<Subtask> subtasksList = new ArrayList<>();
        if (task.getSubtasks().isEmpty()) {
            task.setStartTime(null);
            task.setDuration(null);
            task.setEndTime(null);
        } else {
            for (Integer sbId : subtasksIds) {
                subtasksList.add(subtasks.get(sbId));
            }
            subtasksList.sort(comparator);
            task.setStartTime(subtasksList.get(0).getStartTime());
            task.setDuration(Duration.between(subtasksList.get(0).getStartTime(),
                    subtasksList.get(subtasksList.size() - 1).getEndTime()).toMinutes());
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
                    historyManager.remove(subId);
                    prioritizedTasks.remove(subtasks.get(subId));
                }
                historyManager.remove(epicId);
            }
            epics.clear();
            subtasks.clear();
        } else {
            throw new RuntimeException("В списке нет задач!");
        }
    }

    @Override
    public EpicTask searchEpicTaskById(int id) {
        if (epics.get(id) == null) {
            return null;
        }
        historyManager.linkLast(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void updateEpicTask(int id, EpicTask task) {
        if (epics.containsKey(id) && task != null) {
            epics.get(id).setName(task.getName());
            epics.get(id).setDiscription(task.getDiscription());
            if (!epics.get(id).getSubtasks().isEmpty()) {
                epicTaskSetTime(task);
            }
        } else {
            throw new RuntimeException("Нет задачи с таким ID!");
        }
    }

    @Override
    public void deleteEpicTaskById(int id) {
        if (epics.containsKey(id)) {
            historyManager.remove(id);
            ArrayList<Integer> subtasksId = epics.get(id).getSubtasks();
            for (Integer subtaskId : subtasksId) {
                historyManager.remove(subtaskId);
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        } else {
            throw new RuntimeException("Нет задачи с таким ID!");
        }
    }

    @Override
    public ArrayList<Subtask> getSubTaskOfEpicList(int id) {
        ArrayList<Subtask> result = new ArrayList<>();
        if (epics.containsKey(id)) {
            if (!epics.get(id).getSubtasks().isEmpty()) {
                ArrayList<Integer> subIds = epics.get(id).getSubtasks();
                for (Integer subId : subIds) {
                    result.add(subtasks.get(subId));
                }
            } else {
                throw new RuntimeException("У этой задачи нет подзадач!");
            }
        } else {
            throw new RuntimeException("Нет задачи с таким ID!");
        }
        return result;
    }

    @Override
    public void addSubTask(Subtask task, int epicTaskId) {
        if (!isTaskCrossOther(task) && epics.containsKey(epicTaskId)) {
            id = idGenerator();
            task.setId(id);
            task.setStatus(Status.NEW);
            task.setEpicName(epics.get(epicTaskId).getName());
            subtasks.put(id, task);
            epics.get(epicTaskId).addSubtask(id);
            epicTaskSetTime(epics.get(epicTaskId));
            epicTaskStatus(epics.get(epicTaskId));
            prioritizedTasks.add(task);
        } else if (!epics.containsKey(epicTaskId)) {
            throw new RuntimeException("Отсутствует основная задача!");
        } else {
            throw new RuntimeException("Время выполнения задачи пересекается с другими!");
        }
    }

    @Override
    public void updateSubTask(int id, Subtask task) {
        if (subtasks.containsKey(id) && task != null) {
            var subtask = subtasks.get(id);
            prioritizedTasks.remove(subtask);
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
            epicTaskSetTime(epics.get(subtask.getEpicId()));
            prioritizedTasks.add(subtask);
        } else {
            throw new RuntimeException("Нет задачи с таким ID!");
        }
    }

    @Override
    public Subtask searchSubTaskById(int id) {
        if (subtasks.get(id) == null) {
            return null;
        }
        historyManager.linkLast(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void deleteAllSubTask() {
        if (!subtasks.isEmpty()) {
            for (EpicTask task : epics.values()) {
                task.removeAllSubTasks();
                epicTaskSetTime(task);
                epicTaskStatus(task);
            }
            for (Integer id : subtasks.keySet()) {
                historyManager.remove(id);
                prioritizedTasks.remove(subtasks.get(id));
            }

            subtasks.clear();
        } else {
            throw new RuntimeException("В списке нет подзадач!");
        }
    }

    @Override
    public ArrayList<Subtask> getSubTaskList() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteSubTaskById(int id) {
        if (subtasks.containsKey(id)) {
            prioritizedTasks.remove(subtasks.get(id));
            historyManager.remove(id);
            epics.get(subtasks.get(id).getEpicId()).removeSubTask(subtasks.get(id).getId());
            epicTaskSetTime(epics.get(subtasks.get(id).getEpicId()));
            epicTaskStatus(epics.get(subtasks.get(id).getEpicId()));
            subtasks.remove(id);
        } else {
            throw new RuntimeException("Нет задачи с таким ID!");
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    protected boolean isTaskCrossOther(Task task) {
        boolean isCross = false;
        List<Task> prioritizedTasksList = getPrioritizedTasks();
        for (int i = 0; i < prioritizedTasksList.size(); i++) {
            if (task.getEndTime().isBefore(prioritizedTasksList.get(i).getStartTime()) ||
                    task.getEndTime().equals(prioritizedTasksList.get(i).getStartTime())) {
                isCross = false;
                break;
            } else if (task.getStartTime().isAfter(prioritizedTasksList.get(i).getEndTime()) ||
                    task.getStartTime().equals(prioritizedTasksList.get(i).getEndTime())) {
                if (i == prioritizedTasksList.size() - 1) {
                    isCross = false;
                    break;
                } else if (task.getEndTime().isBefore(prioritizedTasksList.get(i + 1).getStartTime()) ||
                        task.getEndTime().equals(prioritizedTasksList.get(i + 1).getStartTime())) {
                    isCross = false;
                    break;
                } else {
                    continue;
                }
            } else {
                isCross = true;
                break;
            }
        }
        return isCross;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private int idGenerator() {
        id++;
        return id;
    }
}
