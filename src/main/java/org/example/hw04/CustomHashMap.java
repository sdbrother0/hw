package org.example.hw04;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomHashMap<K, V> implements Map<K, V> {

    private static class Entry<K, V> implements Map.Entry<K, V> {
        @Getter
        private K key;
        @Getter
        private V value;

        @Getter
        @Setter
        private Entry<K, V> nextEntry;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

    private Entry<K, V>[] buckets = new Entry[15];

    int elementCount = 0;

    @Override
    public int size() {
        return elementCount;
    }

    @Override
    public boolean isEmpty() {
        return buckets.length == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Entry<K, V> entry : buckets) {
            if (entry.value.equals(value)) {
                return true;
            }
            while (entry != null) {
                if (entry.getValue().equals(value)) {
                    return true;
                }
                entry = entry.getNextEntry();
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        int index = Math.abs(key.hashCode() % buckets.length);
        Entry<K,V> entry = buckets[index];
        while (entry != null) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
            entry = entry.getNextEntry();
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if ((double) size() / buckets.length >= 0.7) {
            remap();
        }
        int index = key.hashCode() % buckets.length;
        Entry<K,V> entry = buckets[index];
        while (entry != null) {
            if (entry.getKey().equals(key)) {
                return entry.setValue(value);
            }
            entry = entry.getNextEntry();
        }
        Entry<K, V> newKeyVal = new Entry<>(key, value);
        newKeyVal.setNextEntry(buckets[index]);
        buckets[index] = newKeyVal;
        elementCount++;
        return value;
    }

    @Override
    public V remove(Object key) {
        int index = key.hashCode() % buckets.length;
        Entry<K, V> entry = buckets[index];
        Entry<K, V> prevEntry = null;

        while (entry != null) {
            if (entry.getKey().equals(key)) {
                if (prevEntry == null) {
                    buckets[index] = entry.getNextEntry();
                } else {
                    prevEntry.setNextEntry(entry.getNextEntry());
                }
                elementCount--;
                return entry.getValue();
            }
            prevEntry = entry;
            entry = entry.getNextEntry();
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = null;
        }
        elementCount = 0;
    }

    @Override
    public Set<K> keySet() {
        return Arrays.stream(buckets)
                .map(Entry::getKey)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values() {
        return Arrays.stream(buckets)
                .map(Entry::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return Arrays
                .stream(buckets)
                .collect(Collectors.toSet());
    }

    private void remap() {
        Entry<K, V>[] newBuckets = new Entry[buckets.length * 2];
        for (Entry<K, V> entry : buckets) {
            while (entry != null) {
                Entry<K, V> next = entry.getNextEntry();
                int index = entry.getKey().hashCode() % newBuckets.length;
                entry.setNextEntry(newBuckets[index]);
                newBuckets[index] = entry;
                entry = next;
            }
        }
        buckets = newBuckets;
    }
}
