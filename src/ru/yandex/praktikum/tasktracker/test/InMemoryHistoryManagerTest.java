package ru.yandex.praktikum.tasktracker.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.tasktracker.data.Status;
import ru.yandex.praktikum.tasktracker.data.Task;
import ru.yandex.praktikum.tasktracker.services.InMemoryHistoryManager;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


class InMemoryHistoryManagerTest {


    InMemoryHistoryManager inMemoryHistoryManager;

    Task task;
    Task task2;
    Task task3;

    @BeforeEach
    void createManagerAndTasksForTest() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
        task = new Task("Test", "Test", Status.NEW,
                LocalDateTime.of(2022, 7, 16, 16, 30), 15L);
        task2 = new Task("Test2", "Test2", Status.NEW,
                LocalDateTime.of(2022, 7, 16, 18, 30), 15L);
        task3 = new Task("Test2", "Test2", Status.NEW,
                LocalDateTime.of(2022, 7, 16, 19, 30), 15L);
    }


    @Test
    void returnEmptyListWhenHistoryIsEmpty() {
        ArrayList<Task> expected = new ArrayList<>();
        ArrayList<Task> actual = (ArrayList<Task>) inMemoryHistoryManager.getHistory();
        assertEquals(expected.size(), actual.size());
    }


    @Test
    void managerDidntSetDublicate() {
        task.setId(1);
        task2.setId(2);
        ArrayList<Task> expected = new ArrayList<>();
        expected.add(task);
        expected.add(task2);
        inMemoryHistoryManager.linkLast(task);
        inMemoryHistoryManager.linkLast(task2);
        inMemoryHistoryManager.linkLast(task);
        inMemoryHistoryManager.linkLast(task2);
        ArrayList<Task> actual = (ArrayList<Task>) inMemoryHistoryManager.getHistory();
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void whenDeleteFirstTaskFromHistory() {
        task.setId(1);
        task2.setId(2);
        task3.setId(3);
        inMemoryHistoryManager.linkLast(task);
        inMemoryHistoryManager.linkLast(task2);
        inMemoryHistoryManager.linkLast(task3);
        inMemoryHistoryManager.remove(1);
        ArrayList<Task> actual = (ArrayList<Task>) inMemoryHistoryManager.getHistory();
        assertEquals(task2, actual.get(0));
    }

    @Test
    void whenDeleteSecondTaskFromHistory() {
        task.setId(1);
        task2.setId(2);
        task3.setId(3);
        inMemoryHistoryManager.linkLast(task);
        inMemoryHistoryManager.linkLast(task2);
        inMemoryHistoryManager.linkLast(task3);
        inMemoryHistoryManager.remove(2);
        ArrayList<Task> actual = (ArrayList<Task>) inMemoryHistoryManager.getHistory();
        assertEquals(task3, actual.get(1));
    }

    @Test
    void whenDeleteLastTaskFromHistory() {
        task.setId(1);
        task2.setId(2);
        task3.setId(3);
        inMemoryHistoryManager.linkLast(task);
        inMemoryHistoryManager.linkLast(task2);
        inMemoryHistoryManager.linkLast(task3);
        inMemoryHistoryManager.remove(3);
        ArrayList<Task> actual = (ArrayList<Task>) inMemoryHistoryManager.getHistory();
        assertEquals(task2, actual.get(1));
    }
}