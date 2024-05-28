package de.felix185.caching;

import java.util.Optional;

/**
 * Simple interface for caches to be implemented.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 * @author Felix Riess <felix@felix-riess.de>
 * @since 28 May 2024
 */
public interface Cache<K, V> {

    /**
     * Upsert an element to the cache with the provided key and value.
     * If an element is already existing for that key, that element will be updated.
     * Else a new element for that key is created.
     *
     * @param key the key of the element.
     * @param value the value to be associated with the key.
     */
    void put(final K key, final V value);

    /**
     * Find an element by its key.
     *
     * @param key the key of the element to be searched.
     * @return {@link Optional} with the element or {@link Optional#empty} if key is not present.
     */
    Optional<V> get(final K key);

    /**
     * Get the size of the cache, i.e. the number of elements in the cache.
     *
     * @return the size of the cache.
     */
    int size();

    /**
     * Remove all elements from the cache.
     */
    void clear();
}
