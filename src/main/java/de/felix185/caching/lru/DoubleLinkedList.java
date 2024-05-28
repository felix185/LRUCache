package de.felix185.caching.lru;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A very simple implementation of a thread-safe double linked list which works as a "queue" for LRU cache.
 * A double linked list is a type of linked list where each node has a reference to its previous <b>and</b> next node.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 * @author Felix Riess <felix@felix-riess.de>
 * @since 28 May 2024
 */
class DoubleLinkedList<K, V> {

    private Node<K, V> head;
    private Node<K, V> tail;
    private int size = 0;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    DoubleLinkedList() {
        this.head = null;
        this.tail = null;
    }

    void addFirst(Node<K, V> node) {
        this.lock.writeLock().lock();
        try {
            if (this.head == null) {
                this.head = node;
                this.tail = node;
            } else {
                node.setNext(this.head);
                this.head.setPrevious(node);
                this.head = node;
            }
            this.size++;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    void moveToFront(Node<K, V> node) {
        this.lock.writeLock().lock();
        try {
            if (node == this.head) {
                return;
            }
            if (node.getPrevious() != null) {
                node.getPrevious().setNext(node.getNext());
            }
            if (node.getNext() != null) {
                node.getNext().setPrevious(node.getPrevious());
            }
            if (node == this.tail) {
                this.tail = node.getPrevious();
            }
            node.setPrevious(null);
            node.setNext(this.head);
            if (this.head != null) {
                this.head.setPrevious(node);
            }
            this.head = node;
            if (this.tail == null) {
                this.tail = node;
            }
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    Node<K, V> removeLast() {
        this.lock.writeLock().lock();
        try {
            if (this.tail == null) {
                return null;
            }
            Node<K, V> node = this.tail;
            this.tail = this.tail.getPrevious();
            if (this.tail != null) {
                this.tail.setNext(null);
            } else {
                this.head = null;
            }
            this.size--;
            return node;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    int size() {
        this.lock.readLock().lock();
        try {
            return this.size;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    void clear() {
        this.lock.writeLock().lock();
        try {
            this.head = null;
            this.tail = null;
            this.size = 0;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

}

