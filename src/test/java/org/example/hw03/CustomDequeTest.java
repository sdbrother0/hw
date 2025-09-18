package org.example.hw03;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Deque;
import java.util.stream.Stream;

class CustomDequeTest {

    private static Stream<Arguments> impls() {
        return Stream.of(
                Arguments.of(new CustomDeque<Integer>()),
                Arguments.of(new CustomDequeArrayListBased<Integer>())
        );
    }

    @ParameterizedTest
    @MethodSource("impls")
    void testCustomStack(Deque<Integer> deque) {
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