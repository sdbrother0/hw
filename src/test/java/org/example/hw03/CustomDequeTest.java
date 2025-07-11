package org.example.hw03;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Deque;

class CustomDequeTest {

    @Test
    void testCustomStack() {
        Deque<Integer> deque = new CustomDeque<>();
        deque.push(1);
        deque.push(2);
        deque.push(3);
        deque.push(4);
        deque.push(5);
        Assertions.assertFalse(deque.isEmpty());
        Assertions.assertEquals(5, deque.pop());
        Assertions.assertEquals(4, deque.pop());
        Assertions.assertEquals(3, deque.pop());
        Assertions.assertEquals(2, deque.peek());
        Assertions.assertEquals(2, deque.peek());
        Assertions.assertEquals(2, deque.pop());
        Assertions.assertEquals(1, deque.pop());
    }
}