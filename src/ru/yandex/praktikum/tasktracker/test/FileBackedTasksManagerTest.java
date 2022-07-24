package ru.yandex.praktikum.tasktracker.test;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.tasktracker.services.FileBackedTasksManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private File file = new File(".\\src\\", "save.csv");

    @Override
    FileBackedTasksManager createManager() throws IOException, InterruptedException {
        return new FileBackedTasksManager(file);
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
        manager = FileBackedTasksManager.loadFromFile(new File(".\\src\\", "save.csv"));
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
        manager.deleteAllSimpleTask();
        BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
        List<String> readFromFile = new ArrayList<>();
        while (br.ready()) {
            readFromFile.add(br.readLine());
        }
        assertEquals("id,type,name,status,description,start_time,end_time,duration,epic", readFromFile.get(0));
        assertEquals("", readFromFile.get(readFromFile.size() - 1));
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
        BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
        List<String> readFromFile = new ArrayList<>();
        readFromFile.clear();
        while (br.ready()) {
            readFromFile.add(br.readLine());
        }
        assertEquals("1,EPIC,TestEpic_1,NEW,TestEpic_1,null,null,null", readFromFile.get(1));
        assertEquals("", readFromFile.get(readFromFile.size() - 1));
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
        BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
        List<String> readFromFile = new ArrayList<>();
        readFromFile.clear();
        while (br.ready()) {
            readFromFile.add(br.readLine());
        }
        assertEquals("1,EPIC,TestEpic_1,NEW,TestEpic_1,2022-07-16T19:30,2022-07-16T19:45,15", readFromFile.get(1));
        assertEquals("2,TASK,TestSimple_1,NEW,Test,2022-07-16T16:30,2022-07-16T16:45,15", readFromFile.get(2));
        assertEquals("3,SUBTASK,TestSubTask_1,NEW,Test,2022-07-16T19:30,2022-07-16T19:45,15,1", readFromFile.get(3));
        assertEquals("", readFromFile.get(readFromFile.size() - 1));
    }

}