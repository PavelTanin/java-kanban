package ru.yandex.praktikum.tasktracker.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {

    protected int id;
    protected String name;
    protected String discription;
    protected Status status;
    protected LocalDateTime startTime;
    protected Long duration;
    protected LocalDateTime endTime;
    final static DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm");

    public Task(String name, String discription, Status status, LocalDateTime startTime, Long duration) {
        this.name = name;
        this.discription = discription;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = startTime.plusMinutes(duration);
        }

    public Task(String name, String discription, Status status) {
        this.name = name;
        this.discription = discription;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
        resetEndTime();
    }

    public void resetEndTime() {
        if (this.duration != null) {
            this.endTime = startTime.plusMinutes(duration);
        } else {
            this.endTime = null;
        }
    }

    public DateTimeFormatter getTIME_FORMAT() {
        return TIME_FORMAT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(discription, task.discription) &&
                Objects.equals(id, task.id) && Objects.equals(status, task.status) && Objects.equals(startTime,
                task.startTime) && Objects.equals(endTime, task.endTime) && Objects.equals(duration,
                task.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, discription, id, status, startTime, duration, endTime);
    }

    @Override
    public String toString() {
        return "Задача номер: " + id + "\n" +
                "Название: " + name + "\n" +
                "Описание: " + discription + "\n" +
                "Состояние: " + status + "\n" +
                "Начало выполнения: " + startTime.format(TIME_FORMAT) + "\n" +
                "Планируемая продолжительность: " + duration+ " минут" + "\n" +
                "Ожидаемое время завершения: " + endTime.format(TIME_FORMAT) + "\n";
    }
}
