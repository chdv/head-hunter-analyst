package com.dch.app.analyst.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ќбертка над java.util.concurrent.ConcurrentHashMap
 * Created by ƒмитрий on 12.06.2015.
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> implements Set<E> {

    private volatile Map<E, Object> map = new ConcurrentHashMap();

    private static final Object PRESENT = new Object();

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o)==PRESENT;
    }

    @Override
    public void clear() {
        map.clear();
    }
}
