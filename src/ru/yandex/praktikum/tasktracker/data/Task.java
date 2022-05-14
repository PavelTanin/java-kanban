package ru.yandex.praktikum.tasktracker.data;

import java.util.Objects;

public class Task {
    protected int id;
    protected String name;
    protected String discription;
    protected String status;

    public Task(String name, String discription, String status) {
        this.name = name;
        this.discription = discription;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(discription, task.discription) &&
                Objects.equals(id, task.id) && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, discription, id);
    }

    @Override
    public String toString() {
        return "Задача: " + id + "\n" +
                "Название: " + name + "\n" +
                "Описание: " + discription + "\n" +
                "Состояние: " + status + "\n";
    }
}
