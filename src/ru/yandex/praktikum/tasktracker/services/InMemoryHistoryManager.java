package ru.yandex.praktikum.tasktracker.services;

import ru.yandex.praktikum.tasktracker.data.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Node<Task> head;
    private Node<Task> tail;


    private final Map<Integer, Node<Task>> nodesList = new HashMap<>();

    @Override
    public void linkLast(Task task) {
        if (nodesList.isEmpty()) {
            Node<Task> newNode = new Node<>(null, task, null);
            final Node<Task> oldHead = head;
            newNode.prevNode = oldHead;
            head = newNode;
            tail = newNode;
            nodesList.put(task.getId(), head);
        } else if (nodesList.containsKey(task.getId())) {
            var recurringNode = nodesList.get(task.getId());
            final Node<Task> oldTail = tail;
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
            Node<Task> newNode = new Node<>(null, task, null);
            final Node<Task> oldTail = tail;
            newNode.prevNode = oldTail;
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.nextNode = newNode;
            }
            nodesList.put(task.getId(), tail);
        }
    }


    @Override
    public void remove(int id) {
        if (nodesList.containsKey(id) && nodesList.get(id) != null) {
            var currentNode = nodesList.get(id);
            if (currentNode.prevNode != null) {
                currentNode.prevNode.nextNode = nodesList.get(id).nextNode;
            } else {
                currentNode.nextNode = head;
                currentNode.nextNode.prevNode = null;
            }
            if (currentNode.nextNode != null) {
                currentNode.nextNode.prevNode = nodesList.get(id).prevNode;
            } else {
                currentNode.prevNode = tail;
                currentNode.prevNode.nextNode = null;
            }
            nodesList.remove(id);
        }
    }


    @Override
    public List<Task> getHistory() {
        if (nodesList.isEmpty()) {
            return new ArrayList<>();
        } else {
            Node<Task> currentNode = head;
            List<Task> result = new ArrayList<>();
            while (!(currentNode == null)) {
                result.add((Task) currentNode.task);
                currentNode = currentNode.nextNode;
            }
            return result;
        }
    }


    public class Node<E> {
        private E task;
        private Node<E> prevNode;
        private Node<E> nextNode;

        public Node(Node prevNode, E task, Node nextNode) {
            this.task = task;
            this.prevNode = prevNode;
            this.nextNode = nextNode;
        }
    }
}


