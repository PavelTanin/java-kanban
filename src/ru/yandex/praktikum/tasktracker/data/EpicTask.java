package ru.yandex.praktikum.tasktracker.data;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;

public class EpicTask extends Task {

    private final ArrayList<Integer> subtasks = new ArrayList<>();



    public EpicTask(String name, String discription, Status status) {
        super(name, discription, null);
        this.startTime = null;
        this.duration = null;
        this.endTime = null;
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
                "Статус: " + status + "\n" +
                nullString();

    }

    private String nullString() {
        if (startTime == null || duration == null || endTime == null) {
            return "Подзадачи данной задачи пока не внесены, невозможно опредилить время выполнения!";
        } else {
            return "Начало выполнения: " + startTime.format(TIME_FORMAT) + "\n" +
                    "Планируемая продолжительность: " + duration.toHours() + " часов " +
                    duration.toMinutesPart() + " минут" + "\n" +
                    "Ожидаемое время завершения: " + endTime.format(TIME_FORMAT) + "\n";
        }
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

