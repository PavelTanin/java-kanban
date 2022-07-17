package ru.yandex.praktikum.tasktracker.test;


import ru.yandex.praktikum.tasktracker.services.InMemoryTaskManager;
import ru.yandex.praktikum.tasktracker.interfaces.TaskManager;


class InMemoryTaskManagerTest extends TaskManagerTest {


    @Override
    TaskManager createManager() {
        return new InMemoryTaskManager();
    }
}