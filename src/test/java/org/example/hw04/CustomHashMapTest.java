package org.example.hw04;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class CustomHashMapTest {

    @Test
    void size() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        Assertions.assertEquals(3, map.size());
    }

    @Test
    void isEmpty() {
        Map<String, Integer> map = new HashMap<>();
        Assertions.assertTrue(map.isEmpty());
    }

    @Test
    void containsKey() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        Assertions.assertTrue(map.containsKey("one"));
        Assertions.assertTrue(map.containsKey("two"));
    }

    @Test
    void containsValue() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        Assertions.assertTrue(map.containsValue(2));
        Assertions.assertTrue(map.containsValue(2));
    }

    @Test
    void get() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        Assertions.assertEquals(2, map.get("two"));
        Assertions.assertEquals(3, map.get("three"));
    }

    @Test
    void put() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        Assertions.assertEquals(3, map.size());
    }

    @Test
    void remove() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        Assertions.assertEquals(3, map.size());
        map.remove("one");
        Assertions.assertEquals(2, map.size());
        Assertions.assertFalse(map.containsKey("one"));
    }

    @Test
    void putAll() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        Assertions.assertEquals(3, map.size());
        Map<String, Integer> map2 = new HashMap<>();
        map2.put("four", 4);
        map2.put("five", 5);
        map2.put("six", 6);
        map.putAll(map2);
        Assertions.assertEquals(6, map.size());
    }

    @Test
    void clear() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        Assertions.assertEquals(3, map.size());
        map.clear();
        Assertions.assertEquals(0, map.size());
        Assertions.assertFalse(map.containsKey("one"));
        Assertions.assertFalse(map.containsKey("two"));
        Assertions.assertFalse(map.containsKey("three"));
    }

    @Test
    void keySet() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        Set<String> keySet = map.keySet();
        Assertions.assertTrue(keySet.contains("one"));
        Assertions.assertTrue(keySet.contains("two"));
        Assertions.assertTrue(keySet.contains("three"));
    }

    @Test
    void values() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        Collection<Integer> values = map.values();
        Assertions.assertTrue(values.contains(1));
        Assertions.assertTrue(values.contains(2));
        Assertions.assertTrue(values.contains(3));
    }

    @Test
    void entrySet() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        Set<Map.Entry<String, Integer>> entrySet = map.entrySet();
        Assertions.assertEquals(3, entrySet.size());
        entrySet.contains(new AbstractMap.SimpleEntry<>("one", 1));
        entrySet.contains(new AbstractMap.SimpleEntry<>("two", 2));
        entrySet.contains(new AbstractMap.SimpleEntry<>("three", 3));
    }
}