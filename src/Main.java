import ru.yandex.praktikum.tasktracker.data.EpicTask;
import ru.yandex.praktikum.tasktracker.data.Status;
import ru.yandex.praktikum.tasktracker.data.Subtask;
import ru.yandex.praktikum.tasktracker.data.Task;
import ru.yandex.praktikum.tasktracker.services.InMemoryTaskManager;

public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Task testSimpleTask = new Task("TestSimple_1", "Test", Status.NEW,
                "16-07-2022, 16:30", 15);
        Task testSimpleTask2 = new Task("TestSimple_2", "Test", Status.NEW,
                "16-07-2022, 15:30", 15);
        Task testSimpleTask3 = new Task("TestSimple_3", "Test", Status.NEW,
                "16-07-2022, 18:30", 15);
        EpicTask testEpicTask = new EpicTask("TestEpic_1", "TestEpic_1", null);
        Subtask testSubtask = new Subtask("TestSubTask_1", "Test", Status.NEW, 1,
                "16-07-2022, 19:30", 15);
        Subtask testSubtask2 = new Subtask("TestSubTask_2", "Test", Status.NEW, 1,
                "16-07-2022, 20:30", 15);

       inMemoryTaskManager.addSimpleTask(testSimpleTask);
       inMemoryTaskManager.deleteSimpleTaskById(1);

        System.out.println(inMemoryTaskManager.getPrioritizedTasks());

    }
}

