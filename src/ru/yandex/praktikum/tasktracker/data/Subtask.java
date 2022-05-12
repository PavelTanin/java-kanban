package ru.yandex.praktikum.tasktracker.data;

import java.util.Objects;

public class Subtask extends Task {

    private int epicId;
    private String epicName;

    public Subtask(String name, String discription, int epicId) {
        super(name, discription);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public String getEpicName() {
        return epicName;
    }

    public void setEpicName(String epicName) {
        this.epicName = epicName;
    }

    @Override
    public String toString() {
        return "Задача номер: " + id + "\n" +
                "Название: " + name + "\n" +
                "Описание: " + discription + "\n" +
                "Подзадача проекта: " + epicNameInfo() + "\n" +
                "Статус: " + status + "\n";
    }

    public String epicNameInfo() {
        if (epicName == null) {
            return "Нет сверхзадач";
        } else {
            return epicName;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(name, subtask.name) && Objects.equals(discription, subtask.discription) &&
                Objects.equals(id, subtask.id) && Objects.equals(status, subtask.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}
