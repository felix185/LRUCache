package de.felix185.caching.lru;

import de.felix185.caching.Cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A thread-safe Last-Recent-Used (LRU) cache implementation.
 * You can image an LRU cache as kind of queue. As soon as an element is accessed (either read or write) the element is moved to the beginning of the queue.
 * The cache has a capacity. As soon as the capacity limit is reached the last element of the "queue" is removed from the cache (i.e. the element which was not
 * touched for the longest time).
 * <p>
 * The {@link #get(Object)} and {@link #put(Object, Object)} operations are run in {@code O(1)}.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value.
 * @author Felix Riess <felix@felix-riess.de>
 * @since 28 May 2024
 */
public class LRUCache<K, V> implements Cache<K, V> {

    private final int capacity;
    private final Map<K, Node<K, V>> cache;
    private final DoubleLinkedList<K, V> list;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>(capacity);
        this.list = new DoubleLinkedList<>();
    }

    @Override
    public Optional<V> get(K key) {
        this.lock.readLock().lock();
        try {
            if (!this.cache.containsKey(key)) {
                return Optional.empty();
            }
            Node<K, V> node = this.cache.get(key);
            this.list.moveToFront(node);
            return Optional.ofNullable(node.getValue());
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public int size() {
        this.lock.readLock().lock();
        try {
            return this.list.size();
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public void clear() {
        this.lock.writeLock().lock();
        try {
            this.cache.clear();
            this.list.clear();
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public void put(K key, V value) {
        this.lock.writeLock().lock();
        try {
            if (this.cache.containsKey(key)) {
                Node<K, V> node = this.cache.get(key);
                node.setValue(value);
                this.list.moveToFront(node);
            } else {
                if (this.list.size() >= this.capacity) {
                    Node<K, V> lastNode = this.list.removeLast();
                    if (lastNode != null) {
                        this.cache.remove(lastNode.getKey());
                    }
                }
                Node<K, V> newNode = new Node<>(key, value);
                this.list.addFirst(newNode);
                this.cache.put(key, newNode);
            }
        } finally {
            this.lock.writeLock().unlock();
        }
    }
}
