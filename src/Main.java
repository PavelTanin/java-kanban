import ru.yandex.praktikum.tasktracker.data.EpicTask;
import ru.yandex.praktikum.tasktracker.data.Subtask;
import ru.yandex.praktikum.tasktracker.services.TaskManager;


public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        EpicTask epic1 = new EpicTask("Учиться", "Пройти курс java-разработчика");
        Subtask sub1 = new Subtask("Спринт 3", "Завершить спринт 3");
        Subtask sub2 = new Subtask("Спринт 4", "Начать спринт 4");

        taskManager.addEpicTask(epic1);
        taskManager.addSubTask(sub1, 1);
        taskManager.addSubTask(sub2, 1);


        EpicTask epic2 = new EpicTask("Ремонт", "Доделать то, что не было сделано во время ремонта");
        Subtask sub3 = new Subtask("Светильник", "Купить и установить светильник для ванной");

        taskManager.addEpicTask(epic2);
        taskManager.addSubTask(sub3, 4);

        System.out.println(taskManager.getSimpleTaskList());
        System.out.println(taskManager.getEpicTaskList());
        System.out.println(taskManager.getSubTaskList());

        taskManager.updateSubTask(2, "Работа почти завершена", "В процессе");
        taskManager.updateSubTask(3, "Думаю что быстро закончу", "Выполнено");

        taskManager.updateSubTask(5, "Я в этом, кажется, ничего не понимаю", "В процессе");

        System.out.println(taskManager.getEpicTaskList());

        taskManager.deleteSubTaskById(2);

        System.out.println(taskManager.getEpicTaskList());

        taskManager.deleteEpicTaskById(4);

        System.out.println(taskManager.getEpicTaskList());
        System.out.println(taskManager.getSubTaskList());
    }
}

