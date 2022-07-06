package ru.yandex.praktikum.tasktracker.services;

import ru.yandex.praktikum.tasktracker.data.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file,
                StandardCharsets.UTF_8))) {
            int key = simpleTasks.size() + epics.size() + subtasks.size();
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
            bw.append("epic");
            bw.append("\n");
            for (int i = 0; i < key; i++) {
                if (simpleTasks.containsKey(i + 1)) {
                    bw.append(toString(simpleTasks.get(i + 1)));
                } else if (epics.containsKey(i + 1)) {
                    bw.append(toString(epics.get(i + 1)));
                } else if (subtasks.containsKey(i + 1)) {
                    bw.append(toString(subtasks.get(i + 1)));
                }
            }
            bw.append("\n");
            bw.append(toString(historyManager));
            if (file.length() == 0) {
                throw new ManagerSaveException("Произошла ошибка при записи файла");
            }
        } catch (ManagerSaveException exception) {

        } catch (IOException exception) {

        }


    }

    private String toString(Task task) {
        String result = null;
        if (task instanceof EpicTask) {
            result = String.format("%s,%s,%s,%s,%s\n", task.getId(), Type.EPIC, task.getName(), task.getStatus(),
                    task.getDiscription());
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            result = String.format("%s,%s,%s,%s,%s,%s\n", subtask.getId(), Type.SUBTASK, subtask.getName(), subtask.getStatus(),
                    subtask.getDiscription(), subtask.getEpicId());
        } else {
            result = String.format("%s,%s,%s,%s,%s\n", task.getId(), Type.TASK, task.getName(), task.getStatus(),
                    task.getDiscription());
        }
        return result;
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
            while (br.ready()) {
                items.add(br.readLine());
            }
            for (int i = 1; i < items.size() - 2; i++) {
                String[] task = items.get(i).split(",");
                if (task[1].equals(String.valueOf(Type.TASK))) {
                    Task newTask = new Task(task[2], task[4], Status.valueOf(task[3]));
                    newTask.setId(Integer.parseInt(task[0]));
                    fileBackedTasksManager.simpleTasks.put(newTask.getId(), newTask);
                } else if (task[1].equals(String.valueOf(Type.EPIC))) {
                    EpicTask newTask = new EpicTask(task[2], task[4], null);
                    newTask.setStatus(Status.valueOf(task[3]));
                    newTask.setId(Integer.parseInt(task[0]));
                    fileBackedTasksManager.epics.put(newTask.getId(), newTask);
                } else if (task[1].equals(String.valueOf(Type.SUBTASK))) {
                    Subtask newTask = new Subtask(task[2], task[4], Status.valueOf(task[3]), Integer.parseInt(task[5]));
                    newTask.setId(Integer.parseInt(task[0]));
                    fileBackedTasksManager.subtasks.put(newTask.getId(), newTask);
                }
            }
            String[] history = items.get(items.size() - 1).split(",");
            for (int i = 0; i < history.length; i++) {
                if (fileBackedTasksManager.simpleTasks.containsKey(history[i])) {
                    Manager.getDefault().searchSimpleTaskById(Integer.parseInt(history[i]));
                } else if (fileBackedTasksManager.epics.containsKey(history[i])) {
                    Manager.getDefault().searchEpicTaskById(Integer.parseInt(history[i]));
                } else if (fileBackedTasksManager.subtasks.containsKey(history[i])) {
                    Manager.getDefault().searchSubTaskById(Integer.parseInt(history[i]));
                }
            FileBackedTasksManager.id = items.size() - 2;
            }
        } catch (IOException exception) {
        }

        return fileBackedTasksManager;

    }

    public void setId(FileBackedTasksManager fileBackedTasksManager) {
        this.id = simpleTasks.size() + epics.size() + subtasks.size();
    }

    public static void main(String[] args) throws IOException {

        Path path = Paths.get("save.csv");

        File file = new File(".\\src\\", "save.csv");

        FileBackedTasksManager fileBackedTasksManager;


        if (!file.exists()) {
            file.createNewFile();
            fileBackedTasksManager = new FileBackedTasksManager(file);
        } else {
            fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
            return;
        }

        Task task = new Task("Спринт 6", "Дописать код", Status.NEW);
        EpicTask epicTask = new EpicTask("Спринт 7", "Завершить новый спринт", null);
        Subtask subtask = new Subtask("Теория", "Уделять 2 часа в день изучению материала", Status.NEW, 2);
        fileBackedTasksManager.addSimpleTask(task);
        fileBackedTasksManager.addEpicTask(epicTask);
        fileBackedTasksManager.searchSimpleTaskById(1);
        fileBackedTasksManager.searchEpicTaskById(2);
        fileBackedTasksManager.addSubTask(subtask, 2);

    }
}


