package ru.yandex.praktikum.tasktracker.services;

import com.google.gson.Gson;
import ru.yandex.praktikum.tasktracker.data.*;
import ru.yandex.praktikum.tasktracker.exceptions.KVClientStartException;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HttpTaskManager extends FileBackedTasksManager {

    private final KVTaskClient kvTaskClient;
    private final URI url;
    private final String key;

    public HttpTaskManager(String url, String key) {
        this.url = URI.create(url);
        this.key = key;
        try {
            this.kvTaskClient = new KVTaskClient(url, key);
        } catch (IOException | InterruptedException e) {
            throw new KVClientStartException("Произошла ошибка при попытке подключиться к серверу");
        }
    }

    @Override
    protected void save() {
        Gson gson = new Gson();
        Map<Integer, Task> allTasks = new HashMap<>();
        allTasks.putAll(simpleTasks);
        allTasks.putAll(epics);
        allTasks.putAll(subtasks);
        TreeMap<Integer, Task> sortedTasks = new TreeMap<>(allTasks);
        StringBuilder sb = new StringBuilder();
        for (Integer key : sortedTasks.keySet()) {
            sb.append(toString(sortedTasks.get(key)));
        }
        sb.append("\n");
        sb.append(toString(historyManager));
        String readyString = sb.toString();
        String json = gson.toJson(readyString);
        kvTaskClient.put(key, json);
    }

    public static HttpTaskManager loadFromServer(String url, String key) throws IOException, InterruptedException {
        HttpTaskManager httpTaskManager = new HttpTaskManager(url, key);
        KVTaskClient kvTaskClient = new KVTaskClient(url, key);
        String managerString = kvTaskClient.load(key);
        String[] managerLines = managerString.split("\n");
        int countTaskLoad = 0;
        List<String> items = List.of(managerLines);
        if (items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                String[] task = items.get(i).replace("\n\n\"", "").split(",");
                if (task.length > 1) {
                    if (task[1].equals(String.valueOf(Type.TASK))) {
                        countTaskLoad++;
                        Task newTask = new Task(task[2], task[4], Status.valueOf(task[3]),
                                LocalDateTime.parse(task[5]), Long.parseLong(task[7]));
                        newTask.setId(Integer.parseInt(task[0]));
                        httpTaskManager.simpleTasks.put(newTask.getId(), newTask);
                    } else if (task[1].equals(String.valueOf(Type.EPIC))) {
                        countTaskLoad++;
                        EpicTask newTask = new EpicTask(task[2], task[4], Status.valueOf(task[3]));
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
                        httpTaskManager.epics.put(newTask.getId(), newTask);
                    } else if (task[1].equals(String.valueOf(Type.SUBTASK))) {
                        countTaskLoad++;
                        Subtask newTask = new Subtask(task[2], task[4], Status.valueOf(task[3]),
                                Integer.parseInt(task[8]), LocalDateTime.parse(task[5]), Long.parseLong(task[7]));
                        newTask.setStatus(Status.valueOf(task[3]));
                        newTask.setId(Integer.parseInt(task[0]));
                        httpTaskManager.subtasks.put(newTask.getId(), newTask);
                    }
                }
            }
            if (items.size() > countTaskLoad + 2) {
                String[] history = items.get(items.size() - 1).split(", ");
                for (String s : history) {
                    if (!s.equals("")) {
                        if (httpTaskManager.simpleTasks.containsKey(Integer.parseInt(s))) {
                            httpTaskManager.searchSimpleTaskById(Integer.parseInt(s));
                        } else if (httpTaskManager.epics.containsKey(Integer.parseInt(s))) {
                            httpTaskManager.searchEpicTaskById(Integer.parseInt(s));
                        } else if (httpTaskManager.subtasks.containsKey((Integer.parseInt(s)))) {
                            httpTaskManager.searchSubTaskById(Integer.parseInt(s));
                        }
                        httpTaskManager.id = items.size() - 2;
                    }
                }
            }
        }
        return httpTaskManager;
    }
}
