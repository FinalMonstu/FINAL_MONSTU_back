package com.icetea.MonStu.util;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomTTLCircleCache<K, V> extends LinkedHashMap<K, CustomTTLCircleCache.Entry<V>> {
    private final int maxSize;
    private final long ttlMillis;

    public static class Entry<V> {
        V value;
        long expireAt; // 만료 시각(밀리초)
        Entry(V value, long expireAt) {
            this.value = value;
            this.expireAt = expireAt;
        }
    }

    public CustomTTLCircleCache(int maxSize, long ttlMillis) {
        super(maxSize, 0.75f, false);
        this.maxSize = maxSize;
        this.ttlMillis = ttlMillis;
    }

    public void putValue(K key, V value) {
        long expireAt = System.currentTimeMillis() + ttlMillis;
        super.put(key, new Entry<>(value, expireAt));
    }

    public V getValue(K key) {
        Entry<V> entry = super.get(key);
        if (entry == null) return null;
        if (System.currentTimeMillis() > entry.expireAt) {
            super.remove(key);
            return null;
        }
        return entry.value;
    }

    // put 작업 시 자동 실행
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, Entry<V>> eldest) {
        boolean expired = eldest.getValue().expireAt < System.currentTimeMillis();
        return size() > maxSize || expired;
    }
}
