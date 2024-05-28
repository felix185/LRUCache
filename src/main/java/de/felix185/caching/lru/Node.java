package de.felix185.caching.lru;

/**
 * Represents a node in a double linked list.
 * Knows its associated key and value as well as its next and previous node.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 * @author Felix Riess <felix@felix-riess.de>
 * @since 28 May 2024
 */
class Node<K, V> {

    private final K key;
    private V value;
    private Node<K, V> previous;
    private Node<K, V> next;

    Node(K key, V value) {
        this.key = key;
        this.value = value;
        this.previous = null;
        this.next = null;
    }

    /**
     * Get key
     *
     * @return value of key
     */
    K getKey() {
        return this.key;
    }

    /**
     * Get value
     *
     * @return value of value
     */
    V getValue() {
        return this.value;
    }

    /**
     * Set the value
     *
     * @param value the value to set
     */
    void setValue(V value) {
        this.value = value;
    }

    /**
     * Get previous
     *
     * @return value of previous
     */
    Node<K, V> getPrevious() {
        return this.previous;
    }

    /**
     * Set the previous
     *
     * @param previous the previous to set
     */
    void setPrevious(Node<K, V> previous) {
        this.previous = previous;
    }

    /**
     * Get next
     *
     * @return value of next
     */
    Node<K, V> getNext() {
        return this.next;
    }

    /**
     * Set the next
     *
     * @param next the next to set
     */
    void setNext(Node<K, V> next) {
        this.next = next;
    }
}

