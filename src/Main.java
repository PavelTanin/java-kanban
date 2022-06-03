import ru.yandex.praktikum.tasktracker.data.EpicTask;
import ru.yandex.praktikum.tasktracker.data.Subtask;
import ru.yandex.praktikum.tasktracker.services.InMemoryTaskManager;
import ru.yandex.praktikum.tasktracker.services.Manager;


public class Main {

    public static void main(String[] args) {

        EpicTask epic1 = new EpicTask("Учиться", "Пройти курс java-разработчика", null);
        Subtask sub1 = new Subtask("Спринт 5", "Завершить спринт 5", null,  1);
        Subtask sub2 = new Subtask("Почитать", "Продолжить расширять свои познания Java", null, 1);
        Subtask sub3 = new Subtask("Проверить код", "Убедиться что не осталось комментов", null, 1);

        Manager.getDefault().addEpicTask(epic1);
        Manager.getDefault().addSubTask(sub1, 1);
        Manager.getDefault().addSubTask(sub2, 1);
        Manager.getDefault().addSubTask(sub3, 1);

        EpicTask epic2 = new EpicTask("Ремонт", "Доделать то, что не было сделано во время ремонта", null);


        Manager.getDefault().addEpicTask(epic2);

        Manager.getDefault().searchEpicTaskById(1);
        Manager.getDefault().searchSubTaskById(2);
        Manager.getDefault().searchSubTaskById(3);
        Manager.getDefault().searchSubTaskById(4);
        Manager.getDefault().searchEpicTaskById(5);
        System.out.println(String.valueOf(Manager.getDefault().getHistory()));
        System.out.println("______________________________________");

        Manager.getDefault().searchSubTaskById(2);
        Manager.getDefault().searchEpicTaskById(5);
        Manager.getDefault().searchEpicTaskById(1);
        Manager.getDefault().searchSubTaskById(4);
        Manager.getDefault().searchSubTaskById(3);
        System.out.println(String.valueOf(Manager.getDefault().getHistory()));
        System.out.println("______________________________________");

        Manager.getDefault().deleteEpicTaskById(5);
        System.out.println(String.valueOf(Manager.getDefault().getHistory()));
        System.out.println("______________________________________");

        Manager.getDefault().deleteEpicTaskById(1);
        if (Manager.getDefault().getHistory().isEmpty()) {
            System.out.println("Все работает как надо!");
        } else {
            System.out.println("Что-то не так!");
        }

    }
}

