package ru.yandex.praktikum.tasktracker.services;

import ru.yandex.praktikum.tasktracker.data.*;
import ru.yandex.praktikum.tasktracker.exceptions.ManagerSaveException;
import ru.yandex.praktikum.tasktracker.interfaces.HistoryManager;
import ru.yandex.praktikum.tasktracker.interfaces.TaskManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    @Override
    public void addSimpleTask(Task task) {
        super.addSimpleTask(task);
        save();
    }

    @Override
    public void deleteAllSimpleTask() {
        super.deleteAllSimpleTask();
        save();
    }

    @Override
    public Task searchSimpleTaskById(int id) {
        Task taskToSearch = super.searchSimpleTaskById(id);
        save();
        return taskToSearch;
    }

    @Override
    public void updateSimpleTask(int id, Task task) {
        super.updateSimpleTask(id, task);
        save();
    }

    @Override
    public void deleteSimpleTaskById(int id) {
        super.deleteSimpleTaskById(id);
        save();
    }

    @Override
    public void addEpicTask(EpicTask task) {
        super.addEpicTask(task);
        save();
    }

    @Override
    public void deleteAllEpicTask() {
        super.deleteAllEpicTask();
        save();
    }

    @Override
    public EpicTask searchEpicTaskById(int id) {
        EpicTask taskToSearch = super.searchEpicTaskById(id);
        save();
        return taskToSearch;
    }

    @Override
    public void updateEpicTask(int id, EpicTask task) {
        super.updateEpicTask(id, task);
        save();
    }

    @Override
    public void deleteEpicTaskById(int id) {
        super.deleteEpicTaskById(id);
        save();
    }

    @Override
    public void updateSubTask(int id, Subtask task) {
        super.updateSubTask(id, task);
        save();
    }

    @Override
    public Subtask searchSubTaskById(int id) {
        Subtask taskToSearch = super.searchSubTaskById(id);
        save();
        return taskToSearch;
    }

    @Override
    public void deleteAllSubTask() {
        super.deleteAllSubTask();
        save();
    }

    @Override
    public void addSubTask(Subtask task, int epicTaskId) {
        super.addSubTask(task, epicTaskId);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    private void save() {
        Map<Integer, Task> allTasks = new HashMap<>();
        allTasks.putAll(simpleTasks);
        allTasks.putAll(epics);
        allTasks.putAll(subtasks);
        TreeMap<Integer, Task> sortedTasks = new TreeMap<>(allTasks);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file,
                StandardCharsets.UTF_8))) {
            bw.append("id");
            bw.append(",");
            bw.append("type");
            bw.append(",");
            bw.append("name");
            bw.append(",");
            bw.append("status");
            bw.append(",");
            bw.append("description");
            bw.append(",");
            bw.append("start_time");
            bw.append(",");
            bw.append("end_time");
            bw.append(",");
            bw.append("duration");
            bw.append(",");
            bw.append("epic");
            bw.append("\n");
            for (Integer key : sortedTasks.keySet()) {
                bw.append(toString(sortedTasks.get(key)));
            }
            bw.append("\n");
            bw.append(toString(historyManager));
        } catch (IOException exception) {
            throw new ManagerSaveException("Произошла ошибка при записи файла");
        }
    }

    private String toString(Task task) {
        if (task instanceof EpicTask) {
            if (((EpicTask) task).getSubtasks().isEmpty()) {
                return String.format("%s,%s,%s,%s,%s,null,null,null\n", task.getId(), Type.EPIC, task.getName(),
                        task.getStatus(), task.getDiscription());
            } else {
                return String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", task.getId(), Type.EPIC, task.getName(),
                        task.getStatus(), task.getDiscription(), task.getStartTime(),
                        task.getEndTime(), task.getDuration());
            }
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n", subtask.getId(), Type.SUBTASK, subtask.getName(),
                    subtask.getStatus(), subtask.getDiscription(), subtask.getStartTime(),
                    subtask.getEndTime(), subtask.getDuration(),
                    subtask.getEpicId());
        } else {
            return String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", task.getId(), Type.TASK, task.getName(), task.getStatus(),
                    task.getDiscription(), task.getStartTime(),
                    task.getEndTime(), task.getDuration());
        }
    }

    private String toString(HistoryManager manager) {
        List<Task> history = new ArrayList<>(manager.getHistory());
        List<Integer> idList = new ArrayList<>();
        for (Task item : history) {
            idList.add(item.getId());
        }
        String result = String.valueOf(idList);
        result = result.replace("[", "");
        result = result.replace("]", "");
        return result;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            List<String> items = new ArrayList<>();
            int countTaskLoad = 0;
            while (br.ready()) {
                items.add(br.readLine());
            }
            if (items.size() > 1) {
                for (int i = 1; i < items.size(); i++) {
                    String[] task = items.get(i).split(",");
                    if (task.length > 1) {
                        if (task[1].equals(String.valueOf(Type.TASK))) {
                            countTaskLoad++;
                            Task newTask = new Task(task[2], task[4], Status.valueOf(task[3]), LocalDateTime.parse(task[5]), Long.parseLong(task[7]));
                            newTask.setId(Integer.parseInt(task[0]));
                            fileBackedTasksManager.simpleTasks.put(newTask.getId(), newTask);
                        } else if (task[1].equals(String.valueOf(Type.EPIC))) {
                            countTaskLoad++;
                            EpicTask newTask = new EpicTask(task[2], task[4], null);
                            newTask.setId(Integer.parseInt(task[0]));
                            if (!task[3].equals("null")) {
                                newTask.setStatus(Status.valueOf(task[3]));
                            }
                            if (!task[5].equals("null")) {
                                newTask.setStartTime(LocalDateTime.parse(task[5]));
                                newTask.setDuration(Long.parseLong(task[7]));
                            } else {
                                newTask.setStartTime(null);
                                newTask.setEndTime(null);
                                newTask.setDuration(null);
                            }
                            fileBackedTasksManager.epics.put(newTask.getId(), newTask);
                        } else if (task[1].equals(String.valueOf(Type.SUBTASK))) {
                            countTaskLoad++;
                            Subtask newTask = new Subtask(task[2], task[4], Status.valueOf(task[3]), Integer.parseInt(task[8]),
                                    LocalDateTime.parse(task[5]), Long.parseLong(task[7]));
                            newTask.setId(Integer.parseInt(task[0]));
                            fileBackedTasksManager.subtasks.put(newTask.getId(), newTask);
                        }
                    }
                }
                if (items.size() > countTaskLoad + 2) {
                    String[] history = items.get(items.size() - 1).split(", ");
                    for (String s : history) {
                        if (!s.equals("")) {
                            if (fileBackedTasksManager.simpleTasks.containsKey(Integer.parseInt(s))) {
                                fileBackedTasksManager.searchSimpleTaskById(Integer.parseInt(s));
                            } else if (fileBackedTasksManager.epics.containsKey(Integer.parseInt(s))) {
                                fileBackedTasksManager.searchEpicTaskById(Integer.parseInt(s));
                            } else if (fileBackedTasksManager.subtasks.containsKey((Integer.parseInt(s)))) {
                                fileBackedTasksManager.searchSubTaskById(Integer.parseInt(s));
                            }
                            fileBackedTasksManager.id = items.size() - 2;
                        }
                    }
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Произошла ошибка при чтении файла");
        }
        return fileBackedTasksManager;
    }

}


