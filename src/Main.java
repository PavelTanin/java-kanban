import ru.yandex.praktikum.tasktracker.services.KVServer;
import ru.yandex.praktikum.tasktracker.services.KVTaskClient;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();

        KVTaskClient kvTaskClient = new KVTaskClient("http://localhost:8078", "Test");
        kvTaskClient.put("Test", "Эта строка должна сохранится");
        kvTaskClient.put("Test2", "И эта строка должна сохранится");
        kvTaskClient.put("Test3", "Вероятно, и эта сохранится");
        System.out.println(kvTaskClient.load("Test"));
        System.out.println(kvTaskClient.load("Test2"));
        System.out.println(kvTaskClient.load("Test3"));
        kvTaskClient.put("Test", "А эта обновит предыдущий результат");
        System.out.println(kvTaskClient.load("Test"));
        System.out.println(kvTaskClient.load("Test2"));
        System.out.println(kvTaskClient.load("Test3"));

        kvServer.stop();

    }
}

