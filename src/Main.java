import ru.yandex.praktikum.tasktracker.data.EpicTask;
import ru.yandex.praktikum.tasktracker.data.Subtask;
import ru.yandex.praktikum.tasktracker.services.TaskManager;


public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        EpicTask epic1 = new EpicTask("Учиться", "Пройти курс java-разработчика");
        Subtask sub1 = new Subtask("Спринт 3", "Завершить спринт 3", 1);
        Subtask sub2 = new Subtask("Спринт 4", "Начать спринт 4", 1);

        taskManager.addEpicTask(epic1);
        taskManager.addSubTask(sub1, 1);
        taskManager.addSubTask(sub2, 1);


        EpicTask epic2 = new EpicTask("Ремонт", "Доделать то, что не было сделано во время ремонта");
        Subtask sub3 = new Subtask("Светильник", "Купить и установить светильник для ванной", 4);

        taskManager.addEpicTask(epic2);
        taskManager.addSubTask(sub3, 4);

        System.out.println(String.valueOf(taskManager.getSimpleTaskList()));
        System.out.println(String.valueOf(taskManager.getEpicTaskList()));
        System.out.println(String.valueOf(taskManager.getSubTaskList()));

        Subtask sub4 = new Subtask("Спринт 3", "Работа почти завершена", 1);
        Subtask sub5 = new Subtask("Спринт 4", "Думаю, что быстро закончу", 1);

        taskManager.updateSubTask(2, sub4, "В процессе");
        taskManager.updateSubTask(3, sub5, "Выполнено");

        Subtask sub6 = new Subtask("Светильник", "Я в этом, кажется, ничего не понимаю", 4);

        taskManager.updateSubTask(5, sub6, "В процессе");

        System.out.println(String.valueOf(taskManager.getEpicTaskList()));

        taskManager.deleteSubTaskById(2);

        System.out.println(String.valueOf(taskManager.getEpicTaskList()));

        taskManager.deleteEpicTaskById(4);

        System.out.println(String.valueOf(taskManager.getEpicTaskList()));
        System.out.println(String.valueOf(taskManager.getSubTaskList()));
    }
}

