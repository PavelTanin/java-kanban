/*package ru.yandex.praktikum.tasktracker.test;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.tasktracker.data.EpicTask;
import ru.yandex.praktikum.tasktracker.data.Status;
import ru.yandex.praktikum.tasktracker.data.Subtask;
import ru.yandex.praktikum.tasktracker.services.InMemoryTaskManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTaskTest {

    InMemoryTaskManager manager = new InMemoryTaskManager();

    EpicTask epicTask = new EpicTask("Test", "Test", null);
    Subtask subtask1 = new Subtask("Test", "Test", Status.NEW, 1,
            LocalDateTime.of(2022, 7, 16, 18, 30), 15);
    Subtask subtask2 = new Subtask("Test", "Test", Status.NEW, 1,
            LocalDateTime.of(2022, 7, 16, 19, 30), 15);
    Subtask subtask3 = new Subtask("Test", "Test", Status.NEW, 1,
            LocalDateTime.of(2022, 7, 16, 20, 30), 15);

    @Test
    void statusNewWhenEmptySubtaskList () {
        EpicTask epicTask = new EpicTask("Test", "Test", null);
        manager.addEpicTask(epicTask);
        assertEquals(Status.NEW, epicTask.getStatus());
    }

    @Test
    void statusNewWhenAllSubtaskIsNew () {
        manager.addEpicTask(epicTask);
        manager.addSubTask(subtask1, 1);
        manager.addSubTask(subtask2, 1);
        manager.addSubTask(subtask3, 1);
        assertEquals(Status.NEW, epicTask.getStatus());
    }

    @Test
    void statusDoneWhenAllSubtaskIsDone () {
        manager.addEpicTask(epicTask);
        manager.addSubTask(subtask1, 1);
        manager.addSubTask(subtask2, 1);
        manager.addSubTask(subtask3, 1);
        Subtask subtask4 = new Subtask("Test", "Test", Status.DONE, 1,
                LocalDateTime.of(2022, 7, 16, 18, 30), 15);
        Subtask subtask5 = new Subtask("Test", "Test", Status.DONE, 1,
                LocalDateTime.of(2022, 7, 16, 19, 30), 15);
        Subtask subtask6 = new Subtask("Test", "Test", Status.DONE, 1,
                LocalDateTime.of(2022, 7, 16, 20, 30), 15);
        manager.updateSubTask(2, subtask4);
        manager.updateSubTask(3, subtask5);
        manager.updateSubTask(4, subtask6);
        assertEquals(Status.DONE, epicTask.getStatus());
    }

    @Test
    void statusInProgressWhenSubtaskStatusIsDoneAndNew () {
        manager.addEpicTask(epicTask);
        manager.addSubTask(subtask1, 1);
        manager.addSubTask(subtask2, 1);
        manager.addSubTask(subtask3, 1);
        Subtask subtask4 = new Subtask("Test", "Test", Status.DONE, 1,
                LocalDateTime.of(2022, 7, 16, 18, 30), 15);
        Subtask subtask5 = new Subtask("Test", "Test", Status.DONE, 1,
                LocalDateTime.of(2022, 7, 16, 19, 30), 15);
        manager.updateSubTask(2, subtask4);
        manager.updateSubTask(3, subtask5);
        assertEquals(Status.IN_PROGRESS, epicTask.getStatus());
    }

    @Test
    void statusInProgressWhenAllSubtaskInProgress () {
        manager.addEpicTask(epicTask);
        manager.addSubTask(subtask1, 1);
        manager.addSubTask(subtask2, 1);
        manager.addSubTask(subtask3, 1);
        Subtask subtask4 = new Subtask("Test", "Test", Status.DONE, 1,
                LocalDateTime.of(2022, 7, 16, 18, 30), 15);
        Subtask subtask5 = new Subtask("Test", "Test", Status.DONE, 1,
                LocalDateTime.of(2022, 7, 16, 19, 30), 15);
        Subtask subtask6 = new Subtask("Test", "Test", Status.DONE, 1,
                LocalDateTime.of(2022, 7, 16, 20, 30), 15);
        manager.updateSubTask(2, subtask4);
        manager.updateSubTask(3, subtask5);
        manager.updateSubTask(2, subtask6);
        assertEquals(Status.IN_PROGRESS, epicTask.getStatus());
    }
}*/