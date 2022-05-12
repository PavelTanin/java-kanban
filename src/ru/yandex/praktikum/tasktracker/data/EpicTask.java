package ru.yandex.praktikum.tasktracker.data;

import java.util.ArrayList;
import java.util.Objects;

public class EpicTask extends Task {

    final ArrayList<Subtask> subtasks = new ArrayList<>();

    public EpicTask(String name, String discription) {
        super(name, discription);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask task) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EpicTask epicTask = (EpicTask) o;
        return Objects.equals(name, epicTask.name) && Objects.equals(discription, epicTask.discription) &&
                Objects.equals(id, epicTask.id) && Objects.equals(status, epicTask.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}

