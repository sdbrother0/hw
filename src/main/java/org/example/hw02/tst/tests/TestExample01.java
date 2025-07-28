package org.example.hw02.tst.tests;

import org.example.hw02.tst.utils.annotations.AfterEach;
import org.example.hw02.tst.utils.annotations.ArgSources;
import org.example.hw02.tst.utils.annotations.BeforeEach;
import org.example.hw02.tst.utils.annotations.Test;

import static org.example.hw02.tst.utils.CustomTestRunner.Assert;

public class TestExample01 {

    public static Object[] getArgs() {
        return new Object[]{
                "test1", 1,
                "test2", 2,
        };
    }

    private int b=0;

    @BeforeEach
    public void beforeEach() {
        this.b = 5;
    }

    @AfterEach
    public void afterEach() {
        this.b = 7;
    }

    @ArgSources(methodName = "getArgs")
    @Test
    public void test1(String a, int x) {
        Assert(a, "test1");
        /*
        if (x == 1) {
            Assert(a, "test1");
        }
        if (x == 2) {
            Assert(a, "test2");
        }
        */
    }

    @Test
    public void test2() {
        Assert(1, 1);
        Assert(1, 2);
    }

    @Test
    public void testBefore() {
        Assert(5, this.b);
    }

}
