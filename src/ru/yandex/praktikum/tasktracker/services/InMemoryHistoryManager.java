package ru.yandex.praktikum.tasktracker.services;

import ru.yandex.praktikum.tasktracker.data.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Node head = new Node(null, null, null);
    private Node tail = new Node(null, null, null);


    private Map<Integer, Node> nodesList = new HashMap<>();

    @Override
    public void addTask(Task task) {
        if (nodesList.isEmpty()) {
            head = new Node(null, task, tail);
            tail.prevNode = head;
            nodesList.put(task.getId(), head);
        } else {
            linkLast(new Node<>(null, task, null), task.getId());
        }
    }




    private void linkLast(Node newNode, int id) {
        var recurringNode = nodesList.get(id);
        if (nodesList.containsKey(id)) {
            final Node oldTail = tail;
            tail = recurringNode;
            oldTail.nextNode = tail;
            if (recurringNode.prevNode == null) {
                recurringNode.nextNode.prevNode = null;
                head = recurringNode.nextNode;
            } else {
                recurringNode.prevNode.nextNode = recurringNode.nextNode;
                recurringNode.nextNode.prevNode = recurringNode.prevNode;
            }
            recurringNode.prevNode = oldTail;
            recurringNode.nextNode = null;
        } else {
            final Node oldTail = tail;
            newNode.prevNode = oldTail;
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.nextNode = newNode;
            }
            nodesList.put(id, tail);
        }
    }


    public void remove(int id) {
        removeNode(nodesList.get(id));
        nodesList.remove(id);
    }

    public void removeNode(Node node) {
        if (!(node.prevNode == null)) {
            node.prevNode.nextNode = node.nextNode;
        } else {
            node.nextNode.prevNode = null;
        }
        if (!(node.nextNode == null)) {
            node.nextNode.prevNode = node.prevNode;
        } else {
            node.prevNode.nextNode = null;
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    public List<Task> getTasks() {
        if (nodesList.isEmpty()) {
            return null;
        } else {
            Node currentNode = head;
            List<Task> result = new ArrayList<>();
            while (!(currentNode == null)) {
                result.add((Task) currentNode.task);
                currentNode = currentNode.nextNode;
            }
            return result;
        }
    }


    public class Node<Task> {
        private Task task;
        private Node<Task> prevNode;
        private Node<Task> nextNode;

        public Node(Node prevNode, Task task, Node nextNode) {
            this.task = task;
            this.prevNode = prevNode;
            this.nextNode = nextNode;
        }
    }
}


