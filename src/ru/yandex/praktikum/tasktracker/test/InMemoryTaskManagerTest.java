package ru.yandex.praktikum.tasktracker.test;


import ru.yandex.praktikum.tasktracker.services.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {


    @Override
    InMemoryTaskManager createManager() {
        return new InMemoryTaskManager();
    }
}