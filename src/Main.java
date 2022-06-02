import ru.yandex.praktikum.tasktracker.data.EpicTask;
import ru.yandex.praktikum.tasktracker.data.Subtask;
import ru.yandex.praktikum.tasktracker.services.InMemoryTaskManager;


public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        EpicTask epic1 = new EpicTask("Учиться", "Пройти курс java-разработчика", null);
        Subtask sub1 = new Subtask("Спринт 5", "Завершить спринт 5", null,  1);
        Subtask sub2 = new Subtask("Почитать", "Продолжить расширять свои познания Java", null, 1);
        Subtask sub3 = new Subtask("Проверить код", "Убедиться что не осталось комментов", null, 1);

        taskManager.addEpicTask(epic1);
        taskManager.addSubTask(sub1, 1);
        taskManager.addSubTask(sub2, 1);
        taskManager.addSubTask(sub3, 1);

        EpicTask epic2 = new EpicTask("Ремонт", "Доделать то, что не было сделано во время ремонта", null);


        taskManager.addEpicTask(epic2);

        taskManager.searchEpicTaskById(1);
        taskManager.searchSubTaskById(2);
        taskManager.searchSubTaskById(3);
        taskManager.searchSubTaskById(4);
        taskManager.searchEpicTaskById(5);
        System.out.println(String.valueOf(taskManager.getHistory()));
        System.out.println("______________________________________");

        taskManager.searchSubTaskById(2);
        taskManager.searchEpicTaskById(5);
        taskManager.searchEpicTaskById(1);
        taskManager.searchSubTaskById(4);
        taskManager.searchSubTaskById(3);
        System.out.println(String.valueOf(taskManager.getHistory()));
        System.out.println("______________________________________");

        taskManager.deleteEpicTaskById(5);
        System.out.println(String.valueOf(taskManager.getHistory()));
        System.out.println("______________________________________");

        taskManager.deleteEpicTaskById(1);
        if (taskManager.getHistory() == null) {
            System.out.println("Все работает как надо!");
        } else {
            System.out.println("Что-то не так!");
        }

    }
}

