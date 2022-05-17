package ru.yandex.praktikum.tasktracker.data;

import java.util.ArrayList;
import java.util.Objects;

public class EpicTask extends Task {

    private final ArrayList<Integer> subtasks = new ArrayList<>();

    public EpicTask(String name, String discription, String status) {
        super(name, discription, null);
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Integer id) {
        this.subtasks.add(id);
    }

    public void removeSubTask(Integer id) {
        this.subtasks.remove(id);
    }

    public void removeAllSubTasks() {
        this.subtasks.clear();
    }

    @Override
    public String toString() {
        return "Задача номер: " + id + "\n" +
                "Название: " + name + '\n' +
                "Описание: " + discription + '\n' +
                "Статус: " + status + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EpicTask epicTask = (EpicTask) o;
        return Objects.equals(subtasks, epicTask.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }
}

