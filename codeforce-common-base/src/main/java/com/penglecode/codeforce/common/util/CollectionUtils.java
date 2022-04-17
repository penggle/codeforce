package com.penglecode.codeforce.common.util;

import java.util.*;

/**
 * @author pengpeng
 * @version 1.0
 */
public class CollectionUtils extends org.springframework.util.CollectionUtils {

    /**
     * <p>如果collection为null/empty则返回defaultValue否则原值返回</p>
     * @param collection
     * @param defaultValue
     * @return
     */
    public static <T> Collection<T> defaultIfEmpty(Collection<T> collection, Collection<T> defaultValue) {
        return isEmpty(collection) ? defaultValue : collection;
    }

    /**
     * <p>如果collection为null/empty则返回defaultValue否则原值返回</p>
     * @param collection
     * @param defaultValue
     * @return
     */
    public static <T> List<T> defaultIfEmpty(List<T> collection, List<T> defaultValue) {
        return isEmpty(collection) ? defaultValue : collection;
    }

    /**
     * <p>如果collection为null/empty则返回defaultValue否则原值返回</p>
     * @param collection
     * @param defaultValue
     * @return
     */
    public static <T> Set<T> defaultIfEmpty(Set<T> collection, Set<T> defaultValue) {
        return isEmpty(collection) ? defaultValue : collection;
    }

    /**
     * <p>如果map为null/empty则返回defaultValue否则原值返回</p>
     * @param map
     * @param defaultValue
     * @return
     */
    public static <K,V> Map<K,V> defaultIfEmpty(Map<K,V> map, Map<K,V> defaultValue) {
        return isEmpty(map) ? defaultValue : map;
    }

    /**
     * Adapts an {@code Iterator} to the {@code Enumeration} interface.
     *
     * @param iterator
     * @param <T>
     * @return
     */
    public static <T> Enumeration<T> asEnumeration(final Iterator<T> iterator) {
        Objects.requireNonNull(iterator);
        return new Enumeration<T>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public T nextElement() {
                return iterator.next();
            }
        };
    }

}
