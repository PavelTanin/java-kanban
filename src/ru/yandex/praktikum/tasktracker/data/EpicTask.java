package ru.yandex.praktikum.tasktracker.data;

import java.util.ArrayList;

public class EpicTask extends Task {

    public ArrayList<Subtask> subtasks = new ArrayList<>();

    public EpicTask(String name, String discription) {
        super(name, discription);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtasks(Subtask task) {
        this.subtasks.add(task);
    }

    public void removeSubTask(Subtask subtask) {
        this.subtasks.remove(subtask);
    }

    @Override
    public String toString() {
        return "Задача номер: " + id + "\n" +
                "Название: " + name + '\n' +
                "Описание: " + discription + '\n' +
                "Статус: " + status + '\n' +
                "Подзадачи входящие в эту задачу: " + subTaskInfo() + "\n";
    }

    private String subTaskInfo() {
        if (subtasks.isEmpty()) {
            return "Подзадачи пока не внесены";
        } else {
            return String.valueOf(subtasks);
        }
    }
}

