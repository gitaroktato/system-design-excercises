package com.example.demo.cache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

public class Cache<T> {

    private LinkedHashMap<Object, SoftReference<T>> elements = new LinkedHashMap<>();

    public void put(Object key, T value) {
        elements.put(key, new SoftReference<>(value));
    }

    public T get(Object key) {
        return elements.get(key).get();
    }
}
