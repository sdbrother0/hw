package org.example.hw02.lomb;

import lombok.SneakyThrows;

public class Util {

    public void test(int i) throws Throwable {
        if (i == 0) {
            throw new IllegalArgumentException();
        }
    }

    @SneakyThrows
    public void test0() {
        test(0);
        System.out.println("test ok");
    }
    @SneakyThrows
    public void test1() {
        test(1);
        System.out.println("test ok");
    }

    public void test0_exception() throws Throwable {
        test(0);
        System.out.println("test ok");
    }

    public void test1_exception() throws Throwable {
        test(1);
        System.out.println("test ok");
    }


}
