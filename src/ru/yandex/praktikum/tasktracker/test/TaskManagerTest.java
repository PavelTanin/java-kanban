package ru.yandex.praktikum.tasktracker.test;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.praktikum.tasktracker.data.EpicTask;
import ru.yandex.praktikum.tasktracker.data.Status;
import ru.yandex.praktikum.tasktracker.data.Subtask;
import ru.yandex.praktikum.tasktracker.data.Task;
import ru.yandex.praktikum.tasktracker.interfaces.TaskManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    public T manager;

    abstract T createManager();

    @BeforeEach
    void createTasks() {}
    Task testSimpleTask = new Task("TestSimple_1", "Test", Status.NEW,
            LocalDateTime.of(2022, 7, 16, 16, 30), 15L);
    Task testSimpleTask2 = new Task("TestSimple_2", "Test", Status.NEW,
            LocalDateTime.of(2022, 7, 16, 17, 30), 15L);
    Task testSimpleTask3 = new Task("TestSimple_3", "Test", Status.NEW,
            LocalDateTime.of(2022, 7, 16, 18, 30), 15L);
    EpicTask testEpicTask = new EpicTask("TestEpic_1", "TestEpic_1", null);
    Subtask testSubtask = new Subtask("TestSubTask_1", "Test", Status.NEW, 1,
            LocalDateTime.of(2022, 7, 16, 19, 30), 15L);
    Subtask testSubtask2 = new Subtask("TestSubTask_2", "Test", Status.NEW, 1,
            LocalDateTime.of(2022, 7, 16, 20, 30), 15L);

    @BeforeEach
    private void updateTaskManager() {
        manager = createManager();
    }

    @Test
    void tryToAddSimpleTask() {
        manager.addSimpleTask(testSimpleTask);
        assertNotNull(manager.searchSimpleTaskById(1));
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.addSimpleTask(testSimpleTask);
                    }
                });
        assertEquals("Время выполнения задачи пересекается с другими!", exception.getMessage());
    }

    @Test
    void tryReturnListWithSimpleTasks() {
        manager.addSimpleTask(testSimpleTask);
        manager.addSimpleTask(testSimpleTask2);
        assertFalse(manager.getSimpleTaskList().isEmpty());
        manager.deleteAllSimpleTask();
        assertTrue(manager.getSimpleTaskList().isEmpty());
    }

    @Test
    void tryDeleteAllSimpleTasks() {
        manager.addSimpleTask(testSimpleTask);
        manager.addSimpleTask(testSimpleTask2);
        manager.addSimpleTask(testSimpleTask3);
        manager.deleteAllSimpleTask();
        assertTrue(manager.getSimpleTaskList().isEmpty());
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.deleteAllSimpleTask();
                    }
                });
        assertEquals("В списке нет простых задач!", exception.getMessage());
    }

    @Test
    void TrySearchSimpleTaskWithId() throws IOException {
        manager.addSimpleTask(testSimpleTask);
        testSimpleTask.setId(1);
        assertEquals(testSimpleTask, manager.searchSimpleTaskById(1));
        assertEquals(testSimpleTask, manager.getHistory().get(0));
        assertEquals(null, manager.searchSimpleTaskById(2));
    }

    @Test
    void tryUpdateSimpleTaskWithId() {
        manager.addSimpleTask(testSimpleTask);
        testSimpleTask = new Task("Test 2", "Test", Status.NEW,
                LocalDateTime.of(2022, 7, 16, 16, 30), 15L);
        manager.updateSimpleTask(1, testSimpleTask);
        testSimpleTask.setId(1);
        assertEquals(testSimpleTask, manager.searchSimpleTaskById(1));
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.updateSimpleTask(2, testSimpleTask);
                    }
                });
        assertEquals("Нет задачи с таким ID!", exception.getMessage());
    }

    @Test
    void tryDeleteSimpleTaskById() {
        manager.addSimpleTask(testSimpleTask);
        manager.deleteSimpleTaskById(1);
        assertTrue(manager.getSimpleTaskList().isEmpty());
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.deleteSimpleTaskById(2);
                    }
                });
        assertEquals("Нет задачи с таким ID!", exception.getMessage());
    }

    @Test
    void tryToAddEpicTask() {
        manager.addEpicTask(testEpicTask);
        assertFalse(manager.getEpicTaskList().isEmpty());
        manager.deleteAllEpicTask();
        assertTrue(manager.getEpicTaskList().isEmpty());
    }

    @Test
    void shouldSetCorrectTimeAndStatusForEpicTaskAndNameOfEpicInSubTask() {
        manager.addEpicTask(testEpicTask);
        manager.addSubTask(testSubtask, 1);
        manager.addSubTask(testSubtask2, 1);
        assertEquals(testEpicTask.getName(), manager.searchSubTaskById(2).getEpicName());
        assertEquals(manager.searchEpicTaskById(1).getId(), manager.searchSubTaskById(2).getEpicId());
        assertEquals(testSubtask.getStartTime(), manager.searchEpicTaskById(1).getStartTime());
        assertEquals(testSubtask2.getEndTime(), manager.searchEpicTaskById(1).getEndTime());
        assertEquals(75L, manager.searchEpicTaskById(1).getDuration());
        assertEquals(Status.NEW, manager.searchEpicTaskById(1).getStatus());
        testSubtask.setStatus(Status.DONE);
        manager.updateSubTask(2, testSubtask);
        assertEquals(Status.IN_PROGRESS, manager.searchEpicTaskById(1).getStatus());
        testSubtask2.setStatus(Status.DONE);
        manager.updateSubTask(3, testSubtask2);
        assertEquals(Status.DONE, manager.searchEpicTaskById(1).getStatus());
        manager.deleteSubTaskById(3);
        assertEquals(testSubtask.getStartTime(), manager.searchEpicTaskById(1).getStartTime());
        assertEquals(testSubtask.getEndTime(), manager.searchEpicTaskById(1).getEndTime());
        assertEquals(15L, manager.searchEpicTaskById(1).getDuration());
        manager.deleteSubTaskById(2);
        assertNull(manager.searchEpicTaskById(1).getStartTime());
        assertNull(manager.searchEpicTaskById(1).getEndTime());
        assertNull(manager.searchEpicTaskById(1).getDuration());
        assertEquals(Status.NEW, manager.searchEpicTaskById(1).getStatus());
    }

    @Test
    void tryDeleteAllEpicTaskFromEmptyList() {
        manager.addEpicTask(testEpicTask);
        manager.deleteAllEpicTask();
        assertTrue(manager.getEpicTaskList().isEmpty());
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.deleteAllEpicTask();
                    }
                });
        assertEquals("В списке нет задач!", exception.getMessage());
    }

    @Test
    void trySearchEpicTaskById() throws IOException {
        manager.addEpicTask(testEpicTask);
        testEpicTask.setId(1);
        assertEquals(testEpicTask, manager.searchEpicTaskById(1));
        assertEquals(testEpicTask, manager.getHistory().get(0));
        assertEquals(null, manager.searchEpicTaskById(2));
    }

    @Test
    void tryUpdateEpicTask() {
        manager.addEpicTask(testEpicTask);
        testEpicTask.setDiscription("Test_discription");
        manager.updateEpicTask(1, testEpicTask);
        assertEquals(testEpicTask, manager.searchEpicTaskById(1));
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.updateEpicTask(2, testEpicTask);
                    }
                });
        assertEquals("Нет задачи с таким ID!", exception.getMessage());
    }

    @Test
    void tryDeleteEpicTaskByIdThenDeleteAllSubTasks() {
        manager.addEpicTask(testEpicTask);
        manager.addSubTask(testSubtask, 1);
        manager.addSubTask(testSubtask2, 1);
        assertEquals(2, manager.getSubTaskList().size());
        manager.deleteEpicTaskById(1);
        assertTrue(manager.getEpicTaskList().isEmpty());
        assertTrue(manager.getSubTaskList().isEmpty());
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.updateEpicTask(1, testEpicTask);
                    }
                });
        assertEquals("Нет задачи с таким ID!", exception.getMessage());
    }

    @Test
    void tryGetSubTaskOfEpicList() {
        manager.addEpicTask(testEpicTask);
        manager.addSubTask(testSubtask, 1);
        manager.addSubTask(testSubtask2, 1);
        assertEquals(2, manager.getSubTaskList().size());
        manager.deleteSubTaskById(2);
        manager.deleteSubTaskById(3);
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.getSubTaskOfEpicList(1);
                    }
                });
        assertEquals("У этой задачи нет подзадач!", exception.getMessage());
        final RuntimeException exception2 = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.getSubTaskOfEpicList(2);;
                    }
                });
        assertEquals("Нет задачи с таким ID!", exception2.getMessage());
    }

    @Test
    void tryAddSubTaskWithAndWithoutEpic() {
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.addSubTask(testSubtask, 1);
                    }
                });
        assertEquals("Отсутствует основная задача!", exception.getMessage());
        manager.addEpicTask(testEpicTask);
        manager.addSubTask(testSubtask, 1);
        final RuntimeException exception2 = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.addSubTask(testSubtask, 1);
                    }
                });
        assertEquals("Время выполнения задачи пересекается с другими!", exception2.getMessage());
        assertEquals(manager.searchSubTaskById(2).getEpicName(), manager.searchEpicTaskById(1).getName());
    }

    @Test
    void tryUpdateSubTaskWithCorrectAndIncorrectId() {
        manager.addEpicTask(testEpicTask);
        manager.addSubTask(testSubtask, 1);
        testSubtask = new Subtask(testSubtask.getName(), testSubtask.getDiscription(), Status.DONE, testSubtask.getEpicId(),
                LocalDateTime.of(2022, 7, 16, 21, 30), 30L);
        final RuntimeException exception2 = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.updateSubTask(3, testSubtask);
                    }
                });
        assertEquals("Нет задачи с таким ID!", exception2.getMessage());
        manager.updateSubTask(2, testSubtask);
        assertEquals(manager.searchSubTaskById(2).getStatus(), manager.searchEpicTaskById(1).getStatus());
        assertEquals(manager.searchSubTaskById(2).getStartTime(), manager.searchEpicTaskById(1).getStartTime());
    }

    @Test
    void trySearchSubTaskByCorrectAndIncorrectId() throws IOException {
        manager.addEpicTask(testEpicTask);
        manager.addSubTask(testSubtask, 1);
        testSubtask.setId(2);
        assertEquals(testSubtask, manager.searchSubTaskById(2));
        assertEquals(testSubtask, manager.getHistory().get(0));
        assertEquals(null, manager.searchSubTaskById(3));
    }

    @Test
    void tryDeleteAllSubTask() {
        manager.addEpicTask(testEpicTask);
        manager.addSubTask(testSubtask, 1);
        manager.addSubTask(testSubtask2, 1);
        manager.deleteAllSubTask();
        assertTrue(manager.getSimpleTaskList().isEmpty());
        assertEquals(null, manager.searchEpicTaskById(1).getStartTime());
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.deleteAllSubTask();
                    }
                });
        assertEquals("В списке нет подзадач!", exception.getMessage());
    }

    @Test
    void getSubTaskList() {
        manager.addEpicTask(testEpicTask);
        manager.addSubTask(testSubtask, 1);
        manager.addSubTask(testSubtask2, 1);
        assertFalse(manager.getSubTaskList().isEmpty());
        manager.deleteAllSubTask();
        assertTrue(manager.getSubTaskList().isEmpty());
    }

    @Test
    void deleteSubTaskById() {
        manager.addEpicTask(testEpicTask);
        manager.addSubTask(testSubtask2, 1);
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.deleteSubTaskById(3);
                    }
                });
        assertEquals("Нет задачи с таким ID!", exception.getMessage());
        manager.deleteSubTaskById(2);
        assertTrue(manager.getSimpleTaskList().isEmpty());
    }

    @Test
    void getHistory() throws IOException {
        manager.addSimpleTask(testSimpleTask);
        manager.addEpicTask(testEpicTask);
        manager.addSubTask(testSubtask, 2);
        assertTrue(manager.getHistory().isEmpty());
        manager.searchSimpleTaskById(1);
        manager.searchSubTaskById(3);
        testSubtask.setId(3);
        assertEquals(testSubtask, manager.getHistory().get(1));
    }

    @Test
    void checkForCrossingTest() {
        manager.addSimpleTask(testSimpleTask);
        testSimpleTask.setId(1);
        manager.addSimpleTask(testSimpleTask2);
        testSimpleTask2.setId(2);
        Task testSimpleTask4 = new Task("TestSimple_1", "Test", Status.NEW,
                LocalDateTime.of(2022, 7, 16, 17, 0), 15L);
        manager.addSimpleTask(testSimpleTask4);
        testSimpleTask4.setId(3);
        manager.addEpicTask(testEpicTask);
        testEpicTask.setId(4);
        Subtask testSubTask3 = new Subtask("TestSubTask_2", "Test", Status.NEW, 4,
                LocalDateTime.of(2022, 7, 16, 16, 45), 15L);
        manager.addSubTask(testSubTask3, 4);
        testSimpleTask4.setId(5);
        List<Task> testPriority = manager.getPrioritizedTasks();
        assertEquals(testSubTask3, testPriority.get(1));
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.addSimpleTask(testSimpleTask);
                    }
                });
        assertEquals("Время выполнения задачи пересекается с другими!", exception.getMessage());
    }
}