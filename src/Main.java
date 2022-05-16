import ru.yandex.praktikum.tasktracker.data.EpicTask;
import ru.yandex.praktikum.tasktracker.data.Subtask;
import ru.yandex.praktikum.tasktracker.services.InMemoryTaskManager;


public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        EpicTask epic1 = new EpicTask("Учиться", "Пройти курс java-разработчика", null);
        Subtask sub1 = new Subtask("Спринт 3", "Завершить спринт 3", null,  1);
        Subtask sub2 = new Subtask("Спринт 4", "Начать спринт 4", null, 1);

        taskManager.addEpicTask(epic1);
        taskManager.addSubTask(sub1, 1);
        taskManager.addSubTask(sub2, 1);


        EpicTask epic2 = new EpicTask("Ремонт", "Доделать то, что не было сделано во время ремонта", null);
        Subtask sub3 = new Subtask("Светильник", "Купить и установить светильник для ванной", null, 4);

        taskManager.addEpicTask(epic2);
        taskManager.addSubTask(sub3, 4);

        taskManager.searchEpicTaskById(1);

        System.out.println(String.valueOf(taskManager.getHistory()));

        taskManager.searchSubTaskById(5);

        System.out.println(String.valueOf(taskManager.getHistory()));

        taskManager.searchSubTaskById(2);

        System.out.println(String.valueOf(taskManager.getHistory()));

    }
}

