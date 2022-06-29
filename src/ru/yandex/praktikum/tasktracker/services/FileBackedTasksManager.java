package ru.yandex.praktikum.tasktracker.services;

import ru.yandex.praktikum.tasktracker.data.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }


    @Override
    public void addSimpleTask(Task task) {
        super.addSimpleTask(task);
        save();
    }

    @Override
    public ArrayList<Task> getSimpleTaskList() {
        return super.getSimpleTaskList();
    }

    @Override
    public void deleteAllSimpleTask() {
        super.deleteAllSimpleTask();
    }

    @Override
    public Task searchSimpleTaskById(int id) {
        return super.searchSimpleTaskById(id);
    }

    @Override
    public void updateSimpleTask(int id, Task task) {
        super.updateSimpleTask(id, task);
    }

    @Override
    public void deleteSimpleTaskById(int id) {
        super.deleteSimpleTaskById(id);
    }

    @Override
    public void addEpicTask(EpicTask task) {
        super.addEpicTask(task);
        save();
    }

    @Override
    public void epicTaskStatus(EpicTask task) {
        super.epicTaskStatus(task);
    }

    @Override
    public ArrayList<EpicTask> getEpicTaskList() {
        return super.getEpicTaskList();
    }

    @Override
    public void deleteAllEpicTask() {
        super.deleteAllEpicTask();
    }

    @Override
    public EpicTask searchEpicTaskById(int id) {
        return super.searchEpicTaskById(id);
    }

    @Override
    public void updateEpicTask(int id, EpicTask task) {
        super.updateEpicTask(id, task);
    }

    @Override
    public void deleteEpicTaskById(int id) {
        super.deleteEpicTaskById(id);
    }

    @Override
    public ArrayList<Subtask> getSubTaskOfEpicList(int id) {
        return super.getSubTaskOfEpicList(id);
    }

    @Override
    public void updateSubTask(int id, Subtask task) {
        super.updateSubTask(id, task);
    }

    @Override
    public Subtask searchSubTaskById(int id) {
        return super.searchSubTaskById(id);
    }

    @Override
    public void deleteAllSubTask() {
        super.deleteAllSubTask();
    }

    @Override
    public void addSubTask(Subtask task, int epicTaskId) {
        super.addSubTask(task, epicTaskId);
        save();
    }

    @Override
    public ArrayList<Subtask> getSubTaskList() {
        return super.getSubTaskList();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file,
                StandardCharsets.UTF_8))) {
            if (simpleTasks.isEmpty() && epics.isEmpty() && subtasks.isEmpty()) {
                throw new ManagerSaveException("Нет записаных задач");
            } else {
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
                bw.append(toString(Manager.getDefaultHistory()));
            }
        } catch (ManagerSaveException | IOException exception) {
            System.out.println(exception.getMessage());
        }


    }

    private String toString(Task task) { //тип в параметры
        return String.format("%s,%s,%s,%s,%s\n", task.getId(), Type.TASK, task.getName(), task.getStatus(),
                task.getDiscription());
    }

    private String toString(EpicTask task) { //тип в параметры
        return String.format("%s,%s,%s,%s,%s\n", task.getId(), Type.EPIC, task.getName(), task.getStatus(),
                task.getDiscription());
    }

    private String toString(Subtask task) { //тип в параметры
        return String.format("%s,%s,%s,%s,%s,%s\n", task.getId(), Type.SUBTASK, task.getName(), task.getStatus(),
                task.getDiscription(), task.getEpicId());
    }

    private String toString(HistoryManager manager) {
        List<Task> history = new ArrayList<>(manager.getHistory());
        List<Integer> idList = new ArrayList<>();
        for (Task item : history) {
            idList.add(item.getId());
        }
        return String.valueOf(idList);
    }

    static FileBackedTasksManager loadFromFile(File file) throws IOException {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            while (br.ready()) {
                stringBuilder.append(br.readLine());
            }
            String line = String.valueOf(stringBuilder);
            String[] items = line.split("\n");
            for (int i = 1; i < items.length - 2; i++) {
                String[] task = items[i].split(",");
                if (task[1].equals(Type.TASK)) {
                    Task newTask = new Task(task[2], task[4], Status.valueOf(task[3]));
                    newTask.setId(Integer.parseInt(task[0]));
                    fileBackedTasksManager.simpleTasks.put(newTask.getId(), newTask);
                } else if (task[1].equals(Type.EPIC)) {
                    EpicTask newTask = new EpicTask(task[2], task[4], null);
                    newTask.setStatus(Status.valueOf(task[3]));
                    newTask.setId(Integer.parseInt(task[0]));
                    fileBackedTasksManager.epics.put(newTask.getId(), newTask);
                } else if (task[1].equals(Type.SUBTASK)) {
                    Subtask newTask = new Subtask(task[2], task[4], Status.valueOf(task[3]), Integer.parseInt(task[5]));
                    newTask.setId(Integer.parseInt(task[0]));
                    fileBackedTasksManager.subtasks.put(newTask.getId(), newTask);
                }
            }
            String[] history = items[items.length - 1].split(",");
            for (int i = 0; i < history.length; i++) {
                if (fileBackedTasksManager.simpleTasks.containsKey(history[i])) {
                    Manager.getDefaultHistory().linkLast(fileBackedTasksManager.simpleTasks.get(history[i]));
                } else if (fileBackedTasksManager.epics.containsKey(history[i])) {
                    Manager.getDefaultHistory().linkLast(fileBackedTasksManager.epics.get(history[i]));
                } else if (fileBackedTasksManager.subtasks.containsKey(history[i])) {
                    Manager.getDefaultHistory().linkLast(fileBackedTasksManager.subtasks.get(history[i]));
                }

            }
            br.close();
            return fileBackedTasksManager;

    }

    public static void main (String[]args) throws IOException {

            Path path = Paths.get("save.csv");

            File file = new File(".\\src\\", "save.csv");

            FileBackedTasksManager fileBackedTasksManager;


            if (!file.exists()) {
                file.createNewFile();
                fileBackedTasksManager = new FileBackedTasksManager(file);
            } else {
                fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
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


