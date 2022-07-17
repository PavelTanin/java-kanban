package ru.yandex.praktikum.tasktracker.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.tasktracker.data.EpicTask;
import ru.yandex.praktikum.tasktracker.data.Status;
import ru.yandex.praktikum.tasktracker.data.Subtask;
import ru.yandex.praktikum.tasktracker.data.Task;
import ru.yandex.praktikum.tasktracker.interfaces.TaskManager;
import ru.yandex.praktikum.tasktracker.services.FileBackedTasksManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class FileBackedTasksManagerTest extends TaskManagerTest {

    private File file = new File(".\\src\\", "testSaveFileEmpty.csv");

    @Override
    TaskManager createManager() {
        return new FileBackedTasksManager(file);
    }

    @Test
    void wouldLoadFromFileWorkCorrect() throws IOException {
        manager = FileBackedTasksManager.loadFromFile(new File(".\\src\\", "testSaveFileEmptyForLoadTest.csv"));
        assertTrue(manager.getSimpleTaskList().isEmpty());
        assertTrue(manager.getEpicTaskList().isEmpty());
        assertTrue(manager.getSubTaskList().isEmpty());
        assertTrue(manager.getHistory().isEmpty());
        manager = new FileBackedTasksManager(new File(".\\src\\", "testSaveFileOnlyEpicForLoadTest.csv"));
        manager.addEpicTask(testEpicTask);
        manager = FileBackedTasksManager.loadFromFile(new File(".\\src\\", "testSaveFileOnlyEpicForLoadTest.csv"));
        testEpicTask.setId(1);
        assertEquals(testEpicTask, manager.getEpicTaskList().get(0));
        manager = manager = FileBackedTasksManager.loadFromFile(new File(".\\src\\",
                "testSaveFileEmptyHistoryForLoadTest.csv"));
        assertEquals(1, manager.getSimpleTaskList().size());
        assertEquals(1, manager.getEpicTaskList().size());
        assertEquals(1, manager.getSubTaskList().size());
        assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    void wouldSaveInFileWorkCorrectWithoutTasks() throws IOException {
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
    void wouldSaveInFileWorkCorrectWithEpicOnly() throws IOException {
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
    void wouldSaveInFileWorkCorrectWithHistory() throws IOException {
        manager.addEpicTask(testEpicTask);
        manager.addSimpleTask(testSimpleTask);
        manager.addSubTask(testSubtask, 1);
        BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
        List<String> readFromFile = new ArrayList<>();
        readFromFile.clear();
        while (br.ready()) {
            readFromFile.add(br.readLine());
        }
        assertEquals("1,EPIC,TestEpic_1,NEW,TestEpic_1,16-07-2022, 19:30,16-07-2022, 19:45,15", readFromFile.get(1));
        assertEquals("2,TASK,TestSimple_1,NEW,Test,16-07-2022, 16:30,16-07-2022, 16:45,15", readFromFile.get(2));
        assertEquals("3,SUBTASK,TestSubTask_1,NEW,Test,16-07-2022, 19:30,16-07-2022, 19:45,15,1", readFromFile.get(3));
        assertEquals("", readFromFile.get(readFromFile.size()-1));
    }

}