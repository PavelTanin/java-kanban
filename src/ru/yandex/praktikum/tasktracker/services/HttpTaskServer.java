package ru.yandex.praktikum.tasktracker.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.praktikum.tasktracker.data.*;
import ru.yandex.praktikum.tasktracker.interfaces.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;

public class HttpTaskServer {

    private final TaskManager taskManager;
    private final HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        this.taskManager = Manager.getDefault();
        httpServer.createContext("/tasks", new TaskHandler());
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + 8080);
        System.out.println("Открой в браузере http://localhost:" + 8080 + "/");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting()
                    .serializeNulls()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
            Gson gson = gsonBuilder.create();
            String response = null;
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            switch (method) {
                case "GET":
                    if (path.equals("/tasks")) {
                        response = gson.toJson(taskManager.getPrioritizedTasks());
                        exchange.sendResponseHeaders(200, 0);
                    } else {
                        String[] fullPath = path.split("/");
                        if (fullPath[2].contains("subtask")) {
                            if (path.contains("id")) {
                                String[] findId = fullPath[2].split("=");
                                response = gson.toJson(taskManager.searchSubTaskById((Integer.parseInt(findId[1]))));
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                response = gson.toJson(taskManager.getSubTaskList());
                                exchange.sendResponseHeaders(200, 0);
                            }
                        } else if (fullPath[2].contains("epic")) {
                            if (fullPath[2].contains("id")) {
                                String[] findId = fullPath[1].split("=");
                                response = gson.toJson(taskManager.searchEpicTaskById((Integer.parseInt(findId[1]))));
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                response = gson.toJson(taskManager.getEpicTaskList());
                                exchange.sendResponseHeaders(200, 0);
                            }
                        } else if (fullPath[2].contains("task")) {
                            if (fullPath[2].contains("id")) {
                                String[] findId = fullPath[1].split("=");
                                response = gson.toJson(taskManager.searchSimpleTaskById((Integer.parseInt(findId[1]))));
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                response = gson.toJson(taskManager.getSimpleTaskList());
                                exchange.sendResponseHeaders(200, 0);
                            }
                        } else if (fullPath[2].contains("history")) {
                            response = gson.toJson(taskManager.getHistory());
                            exchange.sendResponseHeaders(200, 0);
                        }
                    }
                    break;
                case "POST":
                    String[] fullPath = path.split("/");
                    if (fullPath[2].contains("subtask")) {
                        InputStream inputStream = exchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes());
                        Subtask postTask = gson.fromJson(body, Subtask.class);
                        postTask.resetEndTime();
                        if (postTask.getId() > 0) {
                            taskManager.updateSubTask(postTask.getId(), postTask);
                            exchange.sendResponseHeaders(201, 0);
                            response = "Задача успешно обновлена";
                        } else {
                            taskManager.addSubTask(postTask, postTask.getEpicId());
                            exchange.sendResponseHeaders(201, 0);
                            response = "Задача успешно добавлена";

                        }
                        inputStream.close();
                    } else if (fullPath[2].contains("epic")) {
                        InputStream inputStream = exchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes());
                        EpicTask postTask = gson.fromJson(body, EpicTask.class);
                        postTask.resetEndTime();
                        if (postTask.getId() > 0) {
                            taskManager.updateEpicTask(postTask.getId(), postTask);
                            exchange.sendResponseHeaders(201, 0);
                            response = "Задача успешно обновлена";
                        } else {
                            taskManager.addEpicTask(postTask);
                            exchange.sendResponseHeaders(201, 0);
                            response = "Задача успешно добавлена";
                        }
                        inputStream.close();
                    } else if (fullPath[2].contains("task")) {
                        InputStream inputStream = exchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes());
                        Task postTask = gson.fromJson(body, Task.class);
                        postTask.resetEndTime();
                        if (postTask.getId() > 0) {
                            taskManager.updateSimpleTask(postTask.getId(), postTask);
                            exchange.sendResponseHeaders(201, 0);
                            response = "Задача успешно обновлена";
                        } else {
                            taskManager.addSimpleTask(postTask);
                            exchange.sendResponseHeaders(201, 0);
                            response = "Задача успешно добавлена";
                        }
                        inputStream.close();
                    }
                    break;
                case "DELETE":
                    String[] fullPathElements = path.split("/");
                    if (fullPathElements[2].contains("task")) {
                        if (fullPathElements[2].contains("id")) {
                            String[] findId = fullPathElements[1].split("=");
                            taskManager.deleteSimpleTaskById((Integer.parseInt(findId[1])));
                            exchange.sendResponseHeaders(200, 0);
                        } else {
                            taskManager.deleteAllSimpleTask();
                            exchange.sendResponseHeaders(200, 0);
                        }
                    } else if (fullPathElements[2].contains("epic")) {
                        if (path.contains("id")) {
                            String[] findId = fullPathElements[2].split("=");
                            taskManager.deleteEpicTaskById((Integer.parseInt(findId[1])));
                            exchange.sendResponseHeaders(200, 0);
                        } else {
                            taskManager.deleteAllEpicTask();
                            exchange.sendResponseHeaders(200, 0);
                        }
                    } else if (fullPathElements[2].contains("subtask")) {
                        if (path.contains("id")) {
                            String[] findId = fullPathElements[2].split("=");
                            taskManager.deleteSubTaskById((Integer.parseInt(findId[1])));
                            exchange.sendResponseHeaders(200, 0);
                        } else {
                            taskManager.deleteAllSubTask();
                            exchange.sendResponseHeaders(200, 0);
                        }
                    }
                    break;
            }
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}