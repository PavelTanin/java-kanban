package ru.yandex.praktikum.tasktracker.data;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {

    private int epicId;
    private String epicName;

    public Subtask(String name, String discription, Status status, int epicId, LocalDateTime startTime, Long duration) {
        super(name, discription, status, startTime, duration);
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
                "Статус: " + status + "\n" +
                "Начало выполнения: " + startTime.format(TIME_FORMAT) + "\n" +
                "Планируемая продолжительность: " + duration + " минут" + "\n" +
                "Ожидаемое время завершения: " + endTime.format(TIME_FORMAT) + "\n";
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
        return Objects.equals(epicId, subtask.epicId) && Objects.equals(epicName, subtask.epicName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId, epicName);
    }
}
