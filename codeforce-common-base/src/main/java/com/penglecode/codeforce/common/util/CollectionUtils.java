package com.penglecode.codeforce.common.util;

import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author pengpeng
 * @version 1.0.0
 */
@SuppressWarnings("ConstantConditions")
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

    /**
     * 求笛卡尔积
     *
     * @param groupedLists
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> cartesianProduct(List<List<T>> groupedLists) {
        if(isEmpty(groupedLists)) {
            return Collections.emptyList();
        }
        Assert.noNullElements(groupedLists, "The element of 'groupedLists' must be not empty!");
        //利用LinkedList的poll()特性
        LinkedList<List<T>> groupedElements = new LinkedList<>(groupedLists);
        //为递归中的flatMap创造条件,假设groupedElements.poll() => ["1","2","3"], 则startElements => [["1"],["2"],["3"]]
        List<List<T>> startElements = groupedElements.poll().stream()
                .map(Collections::singletonList)
                .collect(Collectors.toList());
        List<List<T>> resultElements = new ArrayList<>();
        recursiveCartesianProduct(startElements, groupedElements, resultElements);
        return resultElements;
    }

    /**
     * 递归求取笛卡尔积
     * @param startElements
     * @param remainingElements
     * @param resultElements
     * @param <T>
     */
    private static <T> void recursiveCartesianProduct(List<List<T>> startElements, LinkedList<List<T>> remainingElements, List<List<T>> resultElements) {
        if(!isEmpty(remainingElements)) {
            List<T> remainingFirst = remainingElements.poll();
            List<List<T>> newStartElements = startElements.stream()
                    //利用flatMap将诸如：[["1"],"红"] ==flatMap()==> ["1","红"]
                    //利用flatMap将诸如：[["1","红"],"A"] ==flatMap()==> ["1","红","A"]
                    .flatMap(startElement -> remainingFirst.stream().map(element -> {
                List<T> subCartesian = new ArrayList<>(startElement);
                subCartesian.add(element);
                return subCartesian;
            })).collect(Collectors.toList());
            recursiveCartesianProduct(newStartElements, remainingElements, resultElements);
        } else {
            resultElements.addAll(startElements);
        }
    }

}