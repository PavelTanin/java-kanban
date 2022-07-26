package ru.yandex.praktikum.tasktracker.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.tasktracker.services.HttpTaskManager;
import ru.yandex.praktikum.tasktracker.services.KVServer;
import ru.yandex.praktikum.tasktracker.services.KVTaskClient;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpTaskManagerTest extends FileBackedTasksManagerTest {

    final KVServer kvServer;

    public HttpTaskManagerTest() throws IOException {
        this.kvServer = new KVServer();
        kvServer.start();

    }

    @Override
    protected HttpTaskManager createManager() throws IOException, InterruptedException {
        return (HttpTaskManager) new HttpTaskManager("http://localhost:8078", "Test");
    }

    @AfterEach
    void stopKVServer() {
        kvServer.stop();
    }

    @Test
    void wouldLoadWorkCorrect() throws IOException, InterruptedException {
        if (!manager.getSimpleTaskList().isEmpty()) {
            manager.deleteAllSimpleTask();
        }
        if (!manager.getEpicTaskList().isEmpty()) {
            manager.deleteAllEpicTask();
        }
        assertTrue(manager.getSimpleTaskList().isEmpty());
        assertTrue(manager.getEpicTaskList().isEmpty());
        assertTrue(manager.getSubTaskList().isEmpty());
        assertTrue(manager.getHistory().isEmpty());
        manager.addEpicTask(testEpicTask);
        manager = HttpTaskManager.loadFromServer("http://localhost:8078", "Test");
        testEpicTask.setId(1);
        assertEquals(testEpicTask, manager.getEpicTaskList().get(0));
        manager.addSubTask(testSubtask, 1);
        manager.addSimpleTask(testSimpleTask);
        assertEquals(1, manager.getSimpleTaskList().size());
        assertEquals(1, manager.getEpicTaskList().size());
        assertEquals(1, manager.getSubTaskList().size());
        assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    void wouldSaveWorkCorrectWithoutTasks() throws IOException, InterruptedException {
        if (!manager.getSimpleTaskList().isEmpty()) {
            manager.deleteAllSimpleTask();
        }
        if (!manager.getEpicTaskList().isEmpty()) {
            manager.deleteAllEpicTask();
        }
        manager.addSimpleTask(testSimpleTask);
        manager.addSimpleTask(testSimpleTask2);
        KVTaskClient newClient = new KVTaskClient("http://localhost:8078", "Test");
        List<String> readFrom = List.of(newClient.load("Test").split("\n"));
        assertEquals("1,TASK,TestSimple_1,NEW,Test,2022-07-16T16:30,2022-07-16T16:45,15", readFrom.get(0));
        assertEquals(2, readFrom.size());
    }

    @Test
    void wouldSaveWorkCorrectWithEpicOnly() throws IOException, InterruptedException {
        if (!manager.getSimpleTaskList().isEmpty()) {
            manager.deleteAllSimpleTask();
        }
        if (!manager.getEpicTaskList().isEmpty()) {
            manager.deleteAllEpicTask();
        }
        manager.addEpicTask(testEpicTask);
        KVTaskClient newClient = new KVTaskClient("http://localhost:8078", "Test");
        List<String> readFrom = List.of(newClient.load("Test").split("\n"));
        assertEquals("1,EPIC,TestEpic_1,NEW,TestEpic_1,null,null,null", readFrom.get(0));
        assertEquals(1, readFrom.size());
    }

    @Test
    void wouldSaveWorkCorrectWithHistory() throws IOException, InterruptedException {
        if (!manager.getSimpleTaskList().isEmpty()) {
            manager.deleteAllSimpleTask();
        }
        if (!manager.getEpicTaskList().isEmpty()) {
            manager.deleteAllEpicTask();
        }
        manager.addEpicTask(testEpicTask);
        manager.addSimpleTask(testSimpleTask);
        manager.addSubTask(testSubtask, 1);
        manager.searchSimpleTaskById(2);
        KVTaskClient newClient = new KVTaskClient("http://localhost:8078", "Test");
        List<String> readFrom = List.of(newClient.load("Test").split("\n"));
        assertEquals("1,EPIC,TestEpic_1,NEW,TestEpic_1,2022-07-16T19:30,2022-07-16T19:45,15", readFrom.get(0));
        assertEquals("2,TASK,TestSimple_1,NEW,Test,2022-07-16T16:30,2022-07-16T16:45,15", readFrom.get(1));
        assertEquals("3,SUBTASK,TestSubTask_1,NEW,Test,2022-07-16T19:30,2022-07-16T19:45,15,1", readFrom.get(2));
        assertEquals("2", readFrom.get(readFrom.size() - 1));
    }
}