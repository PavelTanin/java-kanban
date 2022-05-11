package ru.yandex.praktikum.tasktracker.data;

public class Subtask extends Task {

    private String epicName;

    public Subtask(String name, String discription) {
        super(name, discription);
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
}
